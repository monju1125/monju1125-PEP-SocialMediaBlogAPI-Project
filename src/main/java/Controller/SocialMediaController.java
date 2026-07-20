package Controller;

import Model.*;
import Service.*;

import io.javalin.Javalin;
import io.javalin.http.Context;

/**
 * TODO: You will need to write your own endpoints and handlers for your controller. The endpoints you will need can be
 * found in readme.md as well as the test cases. You should
 * refer to prior mini-project labs and lecture materials for guidance on how a controller may be built.
 */
public class SocialMediaController {
    private final AccountService accountService;
    private final MessageService messageService;

    public SocialMediaController(){
        this.accountService = new AccountService();
        this.messageService = new MessageService();
    }

    /**
     * In order for the test cases to work, you will need to write the endpoints in the startAPI() method, as the test
     * suite must receive a Javalin object from this method.
     * @return a Javalin app object which defines the behavior of the Javalin controller.
     */
    public Javalin startAPI() {
        Javalin app = Javalin.create();
        app.get("example-endpoint", this::exampleHandler);

        app.post("/register", this::registerHandler);
        app.post("/login", this::loginHandler);

        app.post("/messages", this::createMessageHandler);
        app.get("/messages", this::getAllMessagesHandler);
        app.get("/messages/{message_id}", this::getMessageByIdHandler );

        app.delete("/messages/{message_id}", this::deleteMessageByIdHandler);

        app.patch("/messages/{message_id}", this::updateMessageByIdHandler);

        app.get("/accounts/{account_id}/messages", this::getMessagesByAccountIdHandler);

        return app;
    }

    /*
            * Handles user registration
            * registration successful : returns created account
            *Duplicate username return 409
            *Other validation failure returns 400
    */
    private void registerHandler(Context context){
        Account account = context.bodyAsClass(Account.class);

        Account createdAccount = accountService.register(account);

        if (createdAccount == null) {
            context.status(400);
            return;
        }
        context.json(createdAccount);
    }

    /*
          -- comment  --
        * Handles user login
        * Successful login returns matching account
        * Failed login returns 401
    */
    private void loginHandler(Context context) {
        Account account = context.bodyAsClass(Account.class);
        Account loggedInAccount = accountService.login(account);

        if (loggedInAccount == null) {
            context.status(401);
            return;          
        }
        context.json(loggedInAccount);
        
    }

    /*
        * Handles Message Creation
        * Successful creation returns created message
        * Failed creation returns 400
    */
    private void createMessageHandler(Context context){
        Message message = context.bodyAsClass(Message.class);
        Message createdMessage = messageService.createMessage(message);
        
        if (createdMessage == null) {
            context.status(400);
            return;            
        }

        context.json(createdMessage);
    }

    /*
            * Handles retrieving all messages
    */
    private void getAllMessagesHandler(Context context){
        context.json(messageService.getAllMessages());
    }

    /*
        * Handles retrieving one message by id.
        * if no message exists, response body remains empty with status 200.
    */
    private void getMessageByIdHandler(Context context){
        int messageId = Integer.parseInt(context.pathParam("message_id"));
        Message message = messageService.getMessageById(messageId);

        if (message != null) {
            context.json(message);
        }
    }

    /*
         * Handles deleting one message by id.
         * If deleted, return number of rows deleted
         * If not found, response body remains empty with status 200
    */
    private void deleteMessageByIdHandler(Context context){
        int messageId = Integer.parseInt(context.pathParam("message_id"));
            Message deletedMessage =
                                messageService.deletMessageById(messageId);
            
            if (deletedMessage != null) {
                context.json(deletedMessage);
            }
    }

    /*
        * Handles updating a message by Id
        * update successful : return number of rows updated
        * Failed : returns 400
    */
    private void updateMessageByIdHandler(Context context){
        int messageId = Integer.parseInt(context.pathParam("message_id"));
        Message message = context.bodyAsClass(Message.class);

        Message updatedMessage  = 
                        messageService.updatMessageTextById(messageId, message);
        
        if (updatedMessage == null) {
            context.status(400);
            return;
        }

        context.json(updatedMessage);
    }
    /*
        * handles retrieving all message by a specific account id.
    */
    private void getMessagesByAccountIdHandler(Context context){
        int accountId  = Integer.parseInt(context.pathParam("account_id"));
        context.json(messageService.getMessagesByAccountId(accountId));
    }


    /**
     * This is an example handler for an example endpoint.
     * @param context The Javalin Context object manages information about both the HTTP request and response.
     */
    private void exampleHandler(Context context) {
        context.json("sample text");
    }


}