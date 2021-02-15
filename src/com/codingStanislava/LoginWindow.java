package com.codingStanislava;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.FileWriter;
import java.io.IOException;

public class LoginWindow extends JFrame {
    private final ChatClient client;
    JTextField loginField = new JTextField();
    JPasswordField passwordField = new JPasswordField();
    JButton loginButton = new JButton("Login");
    JButton registerButton = new JButton("Register");
    JTextField registerField = new JTextField();
    JPasswordField passwordForRegField = new JPasswordField();
    JButton register = new JButton("Register");
    String username="";
    String password="";

    public LoginWindow() {
        super("Login");

        this.client = new ChatClient("localhost", 8818);
        client.connect();

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JPanel p = new JPanel();
        p.setLayout(new BoxLayout(p, BoxLayout.Y_AXIS));
        p.add(loginField);
        p.add(passwordField);
        p.add(loginButton);
        p.add(registerButton);

        registerButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                doRegister();
            }
        });

        register.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                FileWriter writer = null;
                FileWriter writerPass = null;
                username = registerField.getText();
                password = passwordForRegField.getText();

                String regex = "^.*[A-Z]+.*$";
                boolean result = username.matches(regex);

                String regexPass = "^.*[0-9]+.*$";
                boolean resultPass = password.matches(regexPass);
                try {
                    writer = new FileWriter("usernameData.txt");
                    writerPass = new FileWriter("passwordData.txt");
                    if(password.length()>6 && resultPass==true && username.length()>4 && result == true){
                        writer.write(username);
                        writerPass.write(password);
                    }else{
                        JOptionPane.showMessageDialog( new JFrame(), "Invalid username/password");
                    }
                    writer.close();
                    writerPass.close();
                } catch (IOException ioException) {
                    ioException.printStackTrace();
                }
            }
        });


        loginButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                doLogin();
            }
        });

        getContentPane().add(p, BorderLayout.CENTER);

        pack();

        setVisible(true);
    }

    private void doRegister() {
        JFrame frame = new JFrame("Register");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(120, 120);
        JPanel p2 = new JPanel();
        p2.setLayout(new BoxLayout(p2, BoxLayout.Y_AXIS));

        p2.add(registerField);
        p2.add(passwordForRegField);
        p2.add(register);
        getContentPane().add(p2, BorderLayout.CENTER);
        frame.add(p2);

        frame.setVisible(true);

        setVisible(false); //hide first tab
    }

    private void doLogin() {
        String login = loginField.getText();
        String password = passwordField.getText();

        try {
            if (client.login(login, password)) {
                // bring up the user list window
                UserListPane userListPane = new UserListPane(client);
                JFrame frame = new JFrame("User List");
                frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                frame.setSize(400, 600);
                System.out.println("user is online");
                frame.getContentPane().add(userListPane, BorderLayout.CENTER);
                frame.setVisible(true);

                setVisible(false);
            } else {
                // show error message
                JOptionPane.showMessageDialog(this, "Invalid login/password.");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        LoginWindow loginWin = new LoginWindow();
        loginWin.setVisible(true);
    }
}