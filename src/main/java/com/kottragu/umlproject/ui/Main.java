package com.kottragu.umlproject.ui;

import com.kottragu.umlproject.model.Card;
import com.kottragu.umlproject.model.Purchase;
import com.kottragu.umlproject.model.Status;
import com.kottragu.umlproject.model.Ticket;
import com.kottragu.umlproject.service.PurchaseService;
import com.kottragu.umlproject.service.TicketService;
import com.vaadin.flow.component.applayout.AppLayout;
import com.vaadin.flow.component.applayout.DrawerToggle;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.tabs.Tab;
import com.vaadin.flow.component.tabs.Tabs;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.PWA;
import lombok.extern.slf4j.Slf4j;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;

@Slf4j
@PWA(name = "UmlRailways", shortName = "Railways")
@Route("/")
public class Main extends AppLayout {
    private final TicketService ticketService;
    private final PurchaseService purchaseService;

    Main(TicketService ticketService, PurchaseService purchaseService) {
        this.ticketService = ticketService;
        this.purchaseService = purchaseService;
        DrawerToggle toggle = new DrawerToggle();


        Ticket ticket = new Ticket();
        ticket.setId(1L);
        ticket.setPrice(1000);
        Calendar date = new GregorianCalendar();
        date.set(2021, Calendar.DECEMBER, 27,12,12);
        ticket.setDate(date);
        ticket.setStatus(Status.AVAILABLE);
        ticket.setDirectionFrom("Moscow");
        ticket.setDirectionTo("Saint Petersburg");
        ticketService.save(ticket);

        Ticket ticket1 = new Ticket();
        ticket1.setId(2L);
        ticket1.setPrice(1000);
        Calendar date1 = new GregorianCalendar();
        date1.set(2021, Calendar.DECEMBER, 27,12,12);
        ticket1.setDate(date1);
        ticket1.setStatus(Status.AVAILABLE);
        ticket1.setDirectionFrom("Moscow");
        ticket1.setDirectionTo("Saint Petersburg");
        ticketService.save(ticket1);

        addToDrawer(createTabs());
        addToNavbar(toggle,createTitle());
        addToNavbar(createLogout());
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
        purchaseButton.addClickListener(e -> showCardModal(grid));

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

    public void showCardModal(Grid<Ticket> grid) {
        Dialog cardModal = new Dialog();
        cardModal.getElement().setAttribute("area-label", "Input card data");

        CardUI cardUI = new CardUI(cardModal);
        VerticalLayout dialogLayout = createCardModalLayout(cardUI);
        cardModal.addOpenedChangeListener(event -> {
            if (!event.isOpened() && cardUI.isSubmit()) {
                if (!purchaseService.createPurchase(grid.getSelectedItems(), cardUI.getData())) {
                    Notification.show("Some tickets have already unavailable");
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
        buyButton.addClickListener(e ->
            showCardModal(grid)
        );

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
        grid.setSelectionMode(Grid.SelectionMode.MULTI);

        grid.setItems(data);
        return grid;
    }

}
