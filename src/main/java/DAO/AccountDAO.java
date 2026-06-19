package DAO;

import java.sql.Statement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import Model.Account;
import Util.ConnectionUtil;

public class AccountDAO {
  /*
        *Insert a new account into the darabase
        *@return inserted account with generated account_id, or
        * null if insertion failed
  */
    public Account createAccount(Account account){

      Connection connection = ConnectionUtil.getConnection();

      try{

        String sql = "INSERT INTO account(username, password) VALUES(?, ?)";
        PreparedStatement preparedStatement = 
                            connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);

              preparedStatement.setString(1, account.getUsername());
              preparedStatement.setString(2, account.getPassword());

              int rowsAffected = preparedStatement.executeUpdate();

              if(rowsAffected == 1){
                ResultSet generatedKeys =
                                 preparedStatement.getGeneratedKeys();
                
                                 if(generatedKeys.next()){
                                  int generatedId = generatedKeys.getInt(1);
                                 
                return new Account(generatedId, 
                                   account.getUsername(),
                                   account.getPassword());
              }
            }

      }catch(SQLException e){
        e.printStackTrace();
      }

      return null;
    }

    /*
      Finds an account by username.
    */
    public Account geAccountByUsername(String username){

      Connection connection = ConnectionUtil.getConnection();

      try{
            String sql = "SELECT account_id, username, password FROM account WHERE username = ? ";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);

                  preparedStatement.setString(1, username);

                  ResultSet resultSet = preparedStatement.executeQuery();

                  if (resultSet.next()) {
                    return mapResultSetToAccount(resultSet);
                  }

      }catch(SQLException e){
        e.printStackTrace();
      }

      return null;
    }

  /*
     Finds an account by username and password
  */

  public Account geAccountByUsernameAndPassword(String username, String password){

          Connection connection = ConnectionUtil.getConnection();

        try{
              
              String sql = "SELECT account_id, username, password FROM account WHERE username = ? AND password = ?";
              PreparedStatement preparedStatement = connection.prepareStatement(sql);

                          preparedStatement.setString(1, username);
                          preparedStatement.setString(2, password);

                          ResultSet resultSet = preparedStatement.executeQuery();

                          if(resultSet.next()){
                              return mapResultSetToAccount(resultSet);
                          }

        }catch(SQLException e){
          e.printStackTrace();;
        }
    return null;
  }

  /*
    Finds an account by account_id
  */
  public Account geAccountById(int accountId){
        
      Connection connection = ConnectionUtil.getConnection();

        try{
               String sql = "SELECT account_id, username, password FROM account WHERE account_id = ?";
                PreparedStatement preparedStatement = connection.prepareStatement(sql);

                        preparedStatement.setInt(1, accountId);

                        ResultSet resultSet = preparedStatement.executeQuery();
                        
                        if (resultSet.next()) {
                          return mapResultSetToAccount(resultSet);
                        }

         }catch(SQLException e){
                e.printStackTrace();
         }
     return null;
   }


    /*
      Convert a ResultSet into an Account object
    */
   private Account mapResultSetToAccount(ResultSet resultSet) throws SQLException{
                                                      
        return new Account(
                             resultSet.getInt("account_id"),
                             resultSet.getString("username"),
                             resultSet.getString("password")
                          );      
        }

}
