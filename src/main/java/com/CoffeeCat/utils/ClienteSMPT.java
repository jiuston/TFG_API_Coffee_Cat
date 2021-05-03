package com.CoffeeCat.utils;

import org.apache.commons.net.smtp.AuthenticatingSMTPClient;
import org.apache.commons.net.smtp.SMTPReply;
import org.apache.commons.net.smtp.SimpleSMTPHeader;

import java.io.Writer;

public class ClienteSMPT {

    private String servidor;
    private String nombre;
    private String password;
    private int puerto;
    private String remitente;
    private String asunto;

    public ClienteSMPT(){
        servidor="smtp.gmail.com";
        nombre="noreply.coffeecat";
        password="noreplyCoffeeCat";
        puerto=465;
        remitente="no-reply.coffeecat@gmail.com";
        asunto="Cambiar la contraseña de su cuenta CoffeeCat";
    }

    public boolean enviarCorreoCambioPass(String emailDestino, String codigoRecu){
        boolean isSent=true;

        AuthenticatingSMTPClient client=new AuthenticatingSMTPClient("TLS", true);
        try {
            String cuerpo = "Si has recibido este correo es porque has olvidado tu contraseña, "+
                    "para ello, introduce el codigo: " + codigoRecu + " en tu aplicación";
            int respuesta;
            client.connect(servidor,puerto);
            respuesta = client.getReplyCode();
            if (!SMTPReply.isPositiveCompletion(respuesta)){
                isSent=false;
                throw new Exception("Conexion rechazada");
            }else {
                client.ehlo("localhost");
                client.auth(AuthenticatingSMTPClient.AUTH_METHOD.LOGIN,nombre,password);
                client.setSender(remitente);
                client.addRecipient(emailDestino);
                SimpleSMTPHeader header= new SimpleSMTPHeader(remitente, emailDestino,asunto);
                Writer writer= client.sendMessageData();
                if (writer != null) {
                    writer.write(header.toString());
                    writer.write(cuerpo);
                    writer.close();
                    if (client.completePendingCommand()){
                        respuesta = client.getReplyCode();
                        if (!SMTPReply.isPositiveCompletion(respuesta)){
                            isSent=false;
                            throw new Exception("Fallo al mandar el email "+ client.getReply()+ " -- "+ client.getReplyString());
                        }
                    }
                }else {
                    isSent=false;
                    throw new Exception("Fallo al mandar el email "+client.getReply()+" -- " + client.getReplyString());
                }
            }
            client.disconnect();
        }catch (Exception e){
            isSent=false;
            e.printStackTrace();
        }
        return isSent;
    }
}
