package com.kottragu.umlproject.ui.login;


import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.login.LoginForm;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.BeforeEnterObserver;
import com.vaadin.flow.router.Route;


@Route("login")
public class LoginView extends VerticalLayout implements BeforeEnterObserver {
    private final LoginForm login = new LoginForm();

    public LoginView() {
        setSizeFull();
        setAlignItems(Alignment.CENTER);
        setJustifyContentMode(JustifyContentMode.CENTER);
        login.setAction("login");
        login.addForgotPasswordListener(clicked -> login.getUI().ifPresent(ui -> ui.navigate("password-recovery")));
        Button registration = new Button("Sign up");
        registration.addClickListener(clicked -> registration.getUI().ifPresent(ui -> ui.navigate("registration")));

        H1 welcome = new H1("Welcome");
        welcome.getStyle().set("margin", "0");
        H3 again = new H3("again");
        again.getStyle().set("margin", "0");
        setSpacing(false);
        add(welcome, again, login, registration);
    }


    @Override
    public void beforeEnter(BeforeEnterEvent beforeEnterEvent) {
        if (beforeEnterEvent.getLocation()
                .getQueryParameters()
                .getParameters()
                .containsKey("error")) {
            login.setError(true);
        }
    }

}