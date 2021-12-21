package com.kottragu.umlproject.ui;

import com.kottragu.umlproject.model.Ticket;
import com.vaadin.flow.component.applayout.AppLayout;
import com.vaadin.flow.component.applayout.DrawerToggle;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.tabs.Tab;
import com.vaadin.flow.component.tabs.Tabs;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.PWA;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Slf4j
@PWA(name = "UmlRailways", shortName = "Railways")
@Route("/")
public class Main extends AppLayout {

    Main() {
        DrawerToggle toggle = new DrawerToggle();


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
        tab.getElement().addEventListener("click",event ->
            setContent(createShowTicketsLayout())
        );
        return tab;
    }

    private VerticalLayout createShowTicketsLayout() {
        VerticalLayout layout = new VerticalLayout();
        layout.add(createSimpleGrid());
        Button purchaseButton = new Button("Add to purchase");
        //Create modal window with day limit
        Button bookButton = new Button("Book");
        HorizontalLayout footer = new HorizontalLayout();
        footer.setPadding(true);
        footer.setWidthFull();
        footer.add(bookButton, purchaseButton);
        footer.setJustifyContentMode(FlexComponent.JustifyContentMode.END);
        layout.add(footer);
        return layout;
    }
    private Tab createPurchase() {
        Tab tab = new Tab();
        tab.setLabel("Purchase");
        tab.getElement().addEventListener("click", event ->
                setContent(new Span("Purchase"))
        );
        return tab;
    }

    private Tab createBooked() {
        Tab tab = new Tab();
        tab.setLabel("Booked");
        tab.getElement().addEventListener("click", event -> {
            setContent(new Span("Here will be grid"));
        });
        return tab;
    }

    private Grid<Ticket> createSimpleGrid() {
        Grid<Ticket> grid = new Grid<>(Ticket.class, false);
        grid.addColumn(Ticket::getDirectionFrom).setHeader("From");
        grid.addColumn(Ticket::getDirectionTo).setHeader("To");
        grid.addColumn(Ticket::getDate).setHeader("Date");
        grid.addColumn(Ticket::getPrice).setHeader("Price");
        grid.setSelectionMode(Grid.SelectionMode.MULTI);
        List<Ticket> data = new ArrayList<>();
        Ticket ticket = new Ticket();
        ticket.setId(1L);
        ticket.setAvailable(true);
        ticket.setPrice(1000);
        ticket.setDate(new Date());
        ticket.setDirectionFrom("Moscow");
        ticket.setDirectionTo("Saint Petersburg");
        data.add(ticket);
        grid.setItems(data);
        return grid;
    }
}
