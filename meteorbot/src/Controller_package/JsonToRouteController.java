package Controller_package;

import TI.BoeBot;

import boebot_hardware.Point;
import jdk.nashorn.internal.parser.JSONParser;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Scanner;

public class JsonToRouteController{
    public BufferedWriter out;
    public ArrayList<Point> route = new ArrayList<>();
    public JsonToRouteController() {
            ServerSocket serverSocket = null;
            Socket clientSocket = null;
        try {
            serverSocket = new ServerSocket(8080);
            clientSocket = null;
            while(clientSocket == null){
            clientSocket = serverSocket.accept();
            BoeBot.wait(100);
            }
            BufferedWriter out = new BufferedWriter(new PrintWriter(clientSocket.getOutputStream()));
            BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            StringBuilder stringBuilder = new StringBuilder();
            String input;

            if((input = in.readLine()) != null){
                   stringBuilder.append(input);
                   System.out.println(input);
                   System.out.println("sup");
            }
            System.out.println(stringBuilder.toString());
            Scanner scanner = new Scanner(input);
            scanner.useDelimiter("/");
            while(scanner.hasNext()){
                String string = scanner.next();
                System.out.println(string);
                route.add(new Point(Integer.parseInt(String.valueOf(string.charAt(0))),Integer.parseInt(String.valueOf(string.charAt(2)))));
            }
            clientSocket.close();
            out.close();
            in.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
        finally {
            try {
                    serverSocket.close();
                    clientSocket.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }

        }

    }
}
