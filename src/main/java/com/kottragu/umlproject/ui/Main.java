package com.kottragu.umlproject.ui;

import com.kottragu.umlproject.model.*;
import com.kottragu.umlproject.service.PurchaseService;
import com.kottragu.umlproject.service.TicketScheduling;
import com.kottragu.umlproject.service.TicketService;
import com.kottragu.umlproject.service.UserService;
import com.kottragu.umlproject.ui.login.LoginView;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.applayout.AppLayout;
import com.vaadin.flow.component.applayout.DrawerToggle;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.tabs.Tab;
import com.vaadin.flow.component.tabs.Tabs;
import com.vaadin.flow.component.textfield.NumberField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.component.timepicker.TimePicker;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.PWA;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import java.time.Duration;
import java.time.LocalTime;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;

@Slf4j
@PWA(name = "UmlRailways", shortName = "Railways")
@Route("/")
public class Main extends AppLayout {
    private final TicketService ticketService;
    private final PurchaseService purchaseService;
    private final UserService userService;
    private final TicketScheduling ticketScheduling;
    private User owner;

    Main(TicketService ticketService, PurchaseService purchaseService, UserService userService, TicketScheduling ticketScheduling) {
        this.userService = userService;
        this.ticketService = ticketService;
        this.purchaseService = purchaseService;
        this.ticketScheduling = ticketScheduling;
        DrawerToggle toggle = new DrawerToggle();

        setData();

        addToDrawer(createTabs());
        addToNavbar(toggle,createTitle());
        if (owner.getRole().equals(Role.ADMIN)) {
            addToNavbar(createAdminButton());
        }
        addToNavbar(createLogout());
    }

    private Button createAdminButton() {
        Button admin = new Button("Add timetable");
        admin.addClickListener(e -> showAddTimetableModal());
        admin.getStyle().set("margin-left", "850px");
        admin.getStyle().set("margin-right", "5px");
        return admin;
    }

    private void setData() {
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        owner = userService.getUserByUsername(userDetails.getUsername());
        ticketService.setData();
        purchaseService.setData();
    }

    private H1 createTitle() {
        H1 title = new H1("UmlRailways");

        title.getStyle()
                .set("font-size", "var(--lumo-font-size-l)")
                .set("margin", "0");

        return title;
    }

    private Button createLogout() {
        Button logout = new Button("Logout");
        logout.getStyle().set("margin-left", "auto");
        logout.getStyle().set("margin-right", "5px");
        logout.addClickListener(e -> {
            SecurityContextHolder.clearContext();
            UI.getCurrent().navigate(LoginView.class);
        });
        return logout;
    }

    private Tabs createTabs() {
        Tabs tabs = new Tabs();
        tabs.add(createShowTickets());
        tabs.add(createPurchase());
        tabs.add(createBooked());
        tabs.setOrientation(Tabs.Orientation.VERTICAL);
        return tabs;
    }

    private Tab createShowTickets() {
        Tab tab = new Tab();
        tab.setLabel("Show tickets");
        tab.getElement().addEventListener("click", event ->
            setContent(createShowTicketsLayout())
        );
        return tab;
    }

    private VerticalLayout createShowTicketsLayout() {
        VerticalLayout layout = new VerticalLayout();
        Grid<Ticket> grid = createSimpleTicketGrid(ticketService.getAvailableTickets());
        layout.add(grid);

        Button purchaseButton = new Button("Buy");
        purchaseButton.addClickListener(e -> {
            showCardModal(grid, "Available");
            //grid.setItems(ticketService.getAvailableTickets());
        });

        Button bookButton = new Button("Book");
        bookButton.addClickListener(e -> {
            if(!ticketService.bookTickets(grid.getSelectedItems())) {
                Notification.show("Some tickets have already unavailable");
            } else {
                grid.setItems(ticketService.getAvailableTickets());
            }
        });

        HorizontalLayout footer = new HorizontalLayout();
        footer.setPadding(true);
        footer.setWidthFull();
        footer.add(bookButton, purchaseButton);
        footer.setJustifyContentMode(FlexComponent.JustifyContentMode.END);

        layout.add(footer);

        return layout;
    }

    public void showCardModal(Grid<Ticket> grid, String type) {
        Dialog cardModal = new Dialog();
        cardModal.getElement().setAttribute("area-label", "Input card data");

        CardUI cardUI = new CardUI(cardModal);
        VerticalLayout dialogLayout = createCardModalLayout(cardUI);
        cardModal.addOpenedChangeListener(event -> {
            if (!event.isOpened() && cardUI.isSubmit()) {
                if (!purchaseService.createPurchase(grid.getSelectedItems(), cardUI.getData())) {
                    Notification.show("Some tickets have already unavailable");
                } else {
                    if (type.equals("Available")) {
                        grid.setItems(ticketService.getAvailableTickets());
                    } else if (type.equals("Booked")) {
                        grid.setItems(ticketService.getBookedTickets());
                    }
                }
            }
        });
        cardModal.add(dialogLayout);
        cardModal.open();
    }

    private VerticalLayout createCardModalLayout(CardUI cardUI) {
        VerticalLayout layout = new VerticalLayout();
        layout.add(cardUI);
        return layout;
    }

    private Tab createPurchase() {
        Tab tab = new Tab();
        tab.setLabel("Purchase");
        tab.getElement().addEventListener("click", event ->
                setContent(createPurchaseLayout())
        );
        return tab;
    }

    private VerticalLayout createPurchaseLayout() {
        VerticalLayout mainLayout = new VerticalLayout();
        mainLayout.add(createSimplePurchaseGrid(purchaseService.getPurchases()));
        return mainLayout;
    }

    private Tab createBooked() {
        Tab tab = new Tab();
        tab.setLabel("Booked");
        tab.getElement().addEventListener("click", event -> {
            setContent(createBookedLayout());
        });
        return tab;
    }


    private VerticalLayout createBookedLayout() {
        VerticalLayout mainLayout = new VerticalLayout();
        Grid<Ticket> grid = createSimpleTicketGrid(ticketService.getBookedTickets());
        mainLayout.add(grid);

        Button buyButton = new Button("Buy");
        buyButton.addClickListener(e -> {
            log.info(String.valueOf(grid.getSelectedItems()));
            showCardModal(grid, "Booked");
        });

        Button cancelButton = new Button("Cancel booking");
        cancelButton.addClickListener(e -> {
            ticketService.cancelBook(grid.getSelectedItems());
            grid.setItems(ticketService.getBookedTickets());
        });


        HorizontalLayout footer = new HorizontalLayout();
        footer.setWidthFull();
        footer.setPadding(true);
        footer.setJustifyContentMode(FlexComponent.JustifyContentMode.END);

        footer.add(buyButton, cancelButton);

        mainLayout.add(footer);
        return mainLayout;
    }

    private Grid<Ticket> createSimpleTicketGrid(List<Ticket> data) {
        Grid<Ticket> grid = new Grid<>(Ticket.class, false);
        grid.addColumn(Ticket::getDirectionFrom).setHeader("From");
        grid.addColumn(Ticket::getDirectionTo).setHeader("To");
        grid.addColumn(Ticket::getDateFrontend).setHeader("Date");
        grid.addColumn(Ticket::getPrice).setHeader("Price");
        grid.setSelectionMode(Grid.SelectionMode.MULTI);

        grid.setItems(data);
        return grid;
    }

    private Grid<Purchase> createSimplePurchaseGrid(List<Purchase> data) {
        Grid<Purchase> grid = new Grid<>(Purchase.class, false);
        grid.addColumn(Purchase::getDateFrontend).setHeader("Day of buy");
        grid.addColumn(Purchase::getTotalCost).setHeader("Total cost");
        grid.addColumn(Purchase::getTicketsCount).setHeader("Ticket count");

        grid.setItems(data);
        return grid;
    }

    private void showAddTimetableModal() {
        Dialog modal = new Dialog();
        modal.getElement().setAttribute("aria-label", "Create new timetable");
        modal.add(createAddTimetableLayout(modal));
        modal.open();
    }

    private VerticalLayout createAddTimetableLayout(Dialog modal) {
        H2 headline = new H2("Create new timetable");
        TextField from = new TextField("From");
        TextField to = new TextField("To");
        NumberField frequency = new NumberField("Frequency");
        frequency.setMin(1);

        VerticalLayout fieldLayout = new VerticalLayout(from, to, frequency);
        fieldLayout.setSpacing(false);
        fieldLayout.setPadding(false);
        fieldLayout.setAlignItems(FlexComponent.Alignment.STRETCH);


        //выбор числа
        DatePicker datePicker = new DatePicker("Start date");
        datePicker.setAutoOpen(false);

        //выбор времени
        TimePicker timePicker = new TimePicker();
        timePicker.setLabel("Start time");
        timePicker.setStep(Duration.ofSeconds(1));
        timePicker.setValue(LocalTime.of(15, 45, 8));

        Button cancelButton = new Button("Cancel", e -> modal.close());
        Button applyButton = new Button("Apply", e -> {
            /*ticketScheduling.addTimetable(
                    createTimetableTicket(
                            from.getValue(),
                            to.getValue(),
                            frequency.getValue(),
                            new GregorianCalendar(datePicker.getValue())
                    ));*/
            modal.close();
        });


        HorizontalLayout buttonLayout = new HorizontalLayout(cancelButton,
                applyButton);
        buttonLayout
                .setJustifyContentMode(FlexComponent.JustifyContentMode.END);


        VerticalLayout dialogLayout = new VerticalLayout(headline, fieldLayout, datePicker, timePicker, buttonLayout);
        dialogLayout.setPadding(false);
        dialogLayout.setAlignItems(FlexComponent.Alignment.STRETCH);
        dialogLayout.getStyle().set("width", "300px").set("max-width", "100%");

        return dialogLayout;
    }

    private TimetableTicket createTimetableTicket(String from, String to, int frequency, Calendar startDay, double price) {
        TimetableTicket timetableTicket = new TimetableTicket();
        timetableTicket.setDirectionFrom(from);
        timetableTicket.setDirectionTo(to);
        timetableTicket.setFrequency(frequency);
        timetableTicket.setStartDate(startDay);
        timetableTicket.setPrice(price);
        return timetableTicket;
    }
}
