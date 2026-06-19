package Service;

import DAO.AccountDAO;
import Model.Account;
public class AccountService {

  private final AccountDAO accountDAO;
  public AccountService() {
    this.accountDAO = new AccountDAO();
  }
  /*
      * Register a new account 
      * if valid and username is unique
  */
  public Account register(Account account){
    if (!isValidRegistration(account)) {
      return null;
    }

    Account existingAccount =
                      accountDAO.geAccountByUsername(account.getUsername());
    
    if (existingAccount != null) {
      return null;
    }

    return accountDAO.createAccount(account);
  }

  /*
    * Attempts to log in an account
    * @return matching account, or null if login fails
  */
  public Account login(Account account) {
    if (account == null ||
          account.getUsername() == null ||
          account.getPassword() == null ) {
            return null;
    }
    
    return 
    accountDAO.geAccountByUsernameAndPassword(account.getUsername(), account.getPassword());
  }

  /*
      * Check whether an account exists by id.
      * @return true if account exists 
  */
  public boolean accountExists(int accountId){
    return accountDAO.geAccountById(accountId) != null;
  }

  /*
      Check whether a username already exists.
  */
  public boolean usernameExists(String username) {
    if(username == null){
      return false;
    }

    return
    accountDAO.geAccountByUsername(username) != null;
  }

  /*
    Validates registration input.
  */
  private boolean isValidRegistration(Account account){
        if(account == null) {
          return false;
        }
        if (account.getUsername() == null || 
            account.getUsername().isBlank()) {
              return false;
        }

        return account.getPassword() != null &&
               account.getPassword().length() >= 4;
  }
  
}
