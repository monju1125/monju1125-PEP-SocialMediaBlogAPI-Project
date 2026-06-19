package DAO;
import java.sql.Connection;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import Model.*;
import Util.ConnectionUtil;

public class MessageDAO {

  /*
     insert a new message into the database
     @return inserted message with generated message_id, or
     null if insertion fails
  */

  public Message createMessage(Message message) {
        Connection connection = ConnectionUtil.getConnection();

        try{
              String sql = "INSERT INTO message(posted_by, message_text, time_posted_epoch) VALUES(?, ?, ?)";
              PreparedStatement preparedStatement =
                                          connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
                    
                    preparedStatement.setInt(1, message.getPosted_by());
                    preparedStatement.setString(2, message.getMessage_text());
                    preparedStatement.setLong(3, message.getTime_posted_epoch());
                    
                    int rowsAffected = preparedStatement.executeUpdate();

                    if(rowsAffected == 1){
                      ResultSet generatedKeys = preparedStatement.getGeneratedKeys();

                    if(generatedKeys.next()){
                      int messageId = generatedKeys.getInt(1);

                  return new Message(
                    messageId,
                    message.getPosted_by(),
                    message.getMessage_text(),
                    message.getTime_posted_epoch()
                  );
                    }
              }
        }catch(SQLException e){
          e.printStackTrace();
        }

    return null;
  }

  /*
    Retrieve all messages
    @return list of all messages
  */
  public List<Message> getAllMessages(){
            Connection connection = ConnectionUtil.getConnection();
            List<Message> messages = new ArrayList<>();

          try{

              String sql = "SELECT message_id, posted_by, message_text, time_posted_epoch FROM message";
              PreparedStatement preparedStatement = 
                                              connection.prepareStatement(sql);
                        
              ResultSet resultSet = preparedStatement.executeQuery();

              while(resultSet.next()) {
                messages.add(mapResultSetToMessage(resultSet));
              }
          }catch(SQLException e){
            e.printStackTrace();
          }

    return messages;
  }

  /*
    Retrieves a message by message_id
    @return message if found, otherwise null
  */

  public Message getMessageById(int messageId){

        Connection connection = ConnectionUtil.getConnection();
      
        try{
              String sql = "SELECT message_id, posted_by, message_text, time_posted_epoch FROM message WHERE message_id = ?";
              PreparedStatement preparedStatement = 
                                  connection.prepareStatement(sql);
              
              preparedStatement.setInt(1, messageId);

              ResultSet resultSet = preparedStatement.executeQuery();

              if(resultSet.next()){
                return mapResultSetToMessage(resultSet);
              }

        }catch(SQLException e){
            e.printStackTrace();
        }

    return null;
  }

  /*
    Delete a message by message_id, 
    @return number of row affected
  */
  public Message deletMessageById(int messageId){
    Message messageToDelete = getMessageById(messageId);

    if(messageToDelete == null){
      return null;
    }

        Connection connection = ConnectionUtil.getConnection();

      try{
          String sql = "DELETE FROM message WHERE message_id = ?";
          PreparedStatement preparedStatement = 
                                          connection.prepareStatement(sql);
          
                preparedStatement.setInt(1, messageId);

          int rowsAffected = preparedStatement.executeUpdate();

          if(rowsAffected == 1){
            return messageToDelete;
          }

      }catch(SQLException e){
          e.printStackTrace();
      }
      return null;
    }
  

  /*
    Update message text by message_id. 
    @return number of rows updated
  */

  public Message updatMessageTextById(int messageId, String messageText){

              Connection connection = ConnectionUtil.getConnection();
      
      try{
            String sql = "UPDATE message SET message_text = ? WHERE message_id = ?";
            PreparedStatement preparedStatement = 
                                      connection.prepareStatement(sql);
            
            preparedStatement.setString(1, messageText);
            preparedStatement.setInt(2, messageId);

            int rowsAffected = preparedStatement.executeUpdate();

            if(rowsAffected ==1){
              return getMessageById(messageId);
            }
      }catch(SQLException e){
          e.printStackTrace();
      }

    return null;
  }


  /*
    Retrieves all messages posted by a specific account 
    @return list of messages from the account
  */

  public List<Message> getMessagesByAccountId(int accountId){

                  Connection connection = ConnectionUtil.getConnection();
        List<Message> messages = new ArrayList<>();
      
        try{
              String sql = "SELECT message_id, posted_by, message_text, time_posted_epoch FROM message WHERE posted_by = ?";
              PreparedStatement preparedStatement =
                                  connection.prepareStatement(sql);
              
                      preparedStatement.setInt(1, accountId);

                      ResultSet resultSet = preparedStatement.executeQuery();

                      while (resultSet.next()) {
                        messages.add(mapResultSetToMessage(resultSet));
                      }

        }catch(SQLException e){
              e.printStackTrace();
        }
      return messages;
  }

/*
    *Convert a ResultSet row into a Message object.
    *@return message object
    *@throw SQLException if result access fails
*/

  private Message mapResultSetToMessage (ResultSet resultSet) throws SQLException {

              return new Message(
                resultSet.getInt("message_id"),
                resultSet.getInt("posted_by"),
                resultSet.getString("message_text"),
                resultSet.getLong("time_posted_epoch")
              );
  }
  
}
