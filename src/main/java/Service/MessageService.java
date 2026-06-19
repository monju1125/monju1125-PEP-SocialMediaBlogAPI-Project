package Service;

import DAO.MessageDAO;
import Model.Message;
import java.util.List;

public class MessageService {
    private final MessageDAO messageDAO;
    private final AccountService accountService;

    public MessageService(){
      this.messageDAO = new MessageDAO();
      this.accountService = new AccountService();
    }
  
    /*
      * Create a message if it satisfies validation rules
      * @return created message, or null if invalid
    */
    public Message createMessage (Message message){

      if (!isValidMessageText(message)) {
        return null;
      }

      if (!accountService.accountExists(message.getPosted_by())) {
        return null;
      }

      return messageDAO.createMessage(message);
    }

    /*
      * Get all messages.
      * @return message if found otherwise null 
    */
    public List<Message> getAllMessages() {
      return messageDAO.getAllMessages();
    }

    /*
        Gets a message by id.
        * @return message if found otherwise null
    */
    public Message getMessageById(int messageId){
      return messageDAO.getMessageById(messageId);
    }

    /*
      Deletes a message by id.
      * @return number of rows deleted
    */
    public Message deletMessageById(int messageId){
      return messageDAO.deletMessageById(messageId);
    }

    /*
        * Update a message's text.
        * @return number of rows updated, or 0 if invalid
    */
    public Message updatMessageTextById(int messageId, Message message) {

      if (!isValidMessageText(message)) {
          return null;        
      }

      if (messageDAO.getMessageById(messageId) == null) {
        return null;
      }

      return messageDAO.updatMessageTextById(messageId, message.getMessage_text());
    }

    /*
        * Get all messages by account id
        * @return list of messages 
    */
    public List<Message> getMessagesByAccountId(int accountId){
      return messageDAO.getMessagesByAccountId(accountId);
    }

    /*
      *Validates message text
      * @return true if text is valid
    */
    private boolean isValidMessageText(Message message) {
      if (message == null) {
        return false;
      }

      if (message.getMessage_text() == null || 
          message.getMessage_text().isBlank()) {
        return false;
      }

      return message.getMessage_text().length() <= 255;
    }
  
}
