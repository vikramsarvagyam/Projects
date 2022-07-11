package Project1
import scala.annotation.tailrec
import scala.sys.exit


object Main {
  private var bool : Boolean = false
  private var admin : Boolean = false
  private var currentUsername = ""
  private var changedUsername = ""
  private var oldPassword = ""
  private var passwordCounter = 0

  def main(args: Array[String]): Unit = {
    mysqlDatabase.connect()
    loginMenu()
  }

  @tailrec
  def loginMenu(): Unit = {

    println("Log in/Sign Up")
    println("1 to Login")
    println("2 to sign up as new user")
    println("3 to Quit")
    print("Enter Option: ")
    val option = scala.io.StdIn.readLine()
    option match {
      case "1" => login()
      case "2" => userSignUp()
      case "3" => quitApp()
      case _ => loginMenu()
    }
  }

  def quitApp(): Unit = {
    mysqlDatabase.closeDB()
    exit(0)

  }

  @tailrec
  def userSignUp(): Unit = {
    println("Create a new account!")

    do {
      println("Enter username: ")
      currentUsername = scala.io.StdIn.readLine()
      bool = mysqlDatabase.checkifExists(currentUsername)
      println("Username already exists.")
    }while(bool)


    println("Please enter in your password")
    val password = scala.io.StdIn.readLine()
    val adminVal = 0


    val newAccount = mysqlDatabase.createUser(currentUsername, password, adminVal)
    if (newAccount == 1) {
      println("Account created!")
      loginMenu()
    } else userSignUp()
  }

  @tailrec
  def login(): Unit = {
    print("Enter your username: ")
    currentUsername = scala.io.StdIn.readLine()
    print("Enter your password: ")
    val password = scala.io.StdIn.readLine()

    val validLogin = mysqlDatabase.checkLogin(currentUsername, password)


    if(validLogin){
      println("Login successful! Redirecting you to menu...")
      admin = mysqlDatabase.checkAdmin(currentUsername)

      if (admin){
        adminMenu()
      }
      else {
        basicMenu()
      }
    }

    else{
      println("Credentials are incorrect. Try again")
      login()
    }
  }

  def adminMenu(): Unit = {
    mysqlDatabase.showUsername(currentUsername)
    println("Welcome, " + currentUsername)
    
    println {
        "Admin: " +
        "Option 1: Change username\n" +
        "Option 2: Change password\n" +
        "Option 3: Look at queries \n" +
        "Option 4: Logout\n" +
        "Option 5: exit"
    }

    print("Select an option: ")
    val option = scala.io.StdIn.readLine()
    option match {
      case "1" => changeUsername()
      case "2" => changePassword()
      case "3" => getQueries()
      case "4" => logout()
      case "5" => quitApp()
      case _ => adminMenu()
    }
    adminMenu()
  }
  
  def basicMenu(): Unit = {
    mysqlDatabase.showUsername(currentUsername)
    println("Welcome, " + currentUsername)
    println {
        "Basic: " +
        "Option 1: Change username\n" +
        "Option 2: Change password\n" +
        "Option 3: Look at queries\n" +
        "Option 4: Logout\n" +
        "Option 5: exit"
    }

    print("Select an option: ")
    val option = scala.io.StdIn.readLine()
    option match {
      case "1" => changeUsername()
      case "2" => changePassword()
      case "3" => getQueries()
      case "4" => logout()
      case "5" => quitApp()
      case _ => basicMenu()
    }
    basicMenu()
  }

  def getQueries(): Unit = {
    println("Enter a number between 1-6 to select query: ")
    println("")
    println("1: Display all players stats (points, assists and rebounds)")
    println("2: Display all players names who have played in more than 3 teams")
    println("3: Display the players stats who played more than 50 games and led the league in scoring by average")
    println("4: Display the players who led the league by total points scored in a season")
    println("5: Display top 30 players for a particular season who had the most points, assists and rebounds combined")
    println("6: Display top 50 players with highest shot efficiency for a particular season")

    print("Select a query option: ")
    val choiceQuery = scala.io.StdIn.readLine()
    choiceQuery match {
      case "1" => println("1")
      case "2" => println("2")
      case "3" => println("3")
      case "4" => println("4")
      case "5" => println("5")
      case "6" => println("6")
      case _ =>  getQueries()
    }
    getQueries()
  }


  def changeUsername(): Unit = {
    print("Changing your current username => ")
    mysqlDatabase.showUsername(currentUsername)
    do {
      print("Please enter in your new username: ")
      changedUsername = scala.io.StdIn.readLine()
      bool = mysqlDatabase.checkifExists(changedUsername)
      println("Username already exists. Pick a new one.")
    }while(bool)

    mysqlDatabase.updateUsername(currentUsername,changedUsername)
    currentUsername=changedUsername
  }

  def changePassword(): Unit = {
    print("Changing password for current username => ")
    mysqlDatabase.showUsername(currentUsername)
    do {
      print("Enter current password: ")
      oldPassword = scala.io.StdIn.readLine()
      bool = mysqlDatabase.checkLogin(currentUsername,oldPassword)
      println("Password does not match: Try again.")
      passwordCounter += 1
    }while(!bool || passwordCounter > 3)

    if(!bool) {
      println("Password has been entered wrong multiple times. Try again later.")
    } else {
        print("Enter new password: ")
        val pass = scala.io.StdIn.readLine()
        mysqlDatabase.updatePassword(currentUsername,pass)
    }    
  }

  def logout(): Unit = {
    currentUsername = ""
    changedUsername = ""
    admin = false
    loginMenu()
  }
  
}