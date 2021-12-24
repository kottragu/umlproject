package com.kottragu.umlproject.ui.login;


import com.kottragu.umlproject.model.Role;
import com.kottragu.umlproject.model.User;
import com.kottragu.umlproject.service.UserService;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.login.AbstractLogin;
import com.vaadin.flow.component.login.LoginForm;
import com.vaadin.flow.component.login.LoginI18n;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Route;

@Route("registration")
public class RegistrationView extends VerticalLayout {
    private final LoginForm registration;
    private User user;
    private final UserService userService;


    public RegistrationView(UserService userService) {
        this.userService = userService;
        setSizeFull();
        setAlignItems(Alignment.CENTER);
        setJustifyContentMode(JustifyContentMode.CENTER);
        registration = new LoginForm();
        registration.setI18n(getLoginI18n());
        registration.addForgotPasswordListener(clicked -> registration.getUI().ifPresent(ui -> ui.navigate("login")));
        registration.addLoginListener(this::save);
        add(registration);
    }

    private void save(AbstractLogin.LoginEvent e) {
        if (userService.isUsernameUnique(e.getUsername())) {
            user = new User();
            user.setUsername(e.getUsername());
            user.setPassword(e.getPassword());
            user.setRole(Role.USER);
            userService.saveUser(user);
            Notification notification = new Notification();
            notification.setPosition(Notification.Position.TOP_CENTER);
            notification.setText("Successfully sign up");
            notification.setDuration(5000);
            notification.open();
            UI.getCurrent().navigate("login");
        } else {
            registration.setError(true);
        }
    }
    private LoginI18n getLoginI18n() {
        LoginI18n loginI18n = new LoginI18n();
        loginI18n.setHeader(new LoginI18n.Header());
        loginI18n.setForm(new LoginI18n.Form());
        loginI18n.getForm().setForgotPassword("Already exist?");
        loginI18n.getForm().setUsername("Username");
        loginI18n.getForm().setPassword("Password");
        loginI18n.getForm().setTitle("Sign up");
        loginI18n.getForm().setSubmit("Sign up");
        LoginI18n.ErrorMessage errorMessage = new LoginI18n.ErrorMessage();
        errorMessage.setTitle("Incorrect username");
        errorMessage.setMessage("username already exist");
        loginI18n.setErrorMessage(errorMessage);
        return loginI18n;
    }
}