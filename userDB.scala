package Project1

import java.sql.{Connection, DriverManager, SQLException}
import scala.collection.mutable.ListBuffer
object mysqlDatabase {


  private var connection: Connection = _

  def connect(): Unit = {
    val url = "jdbc:mysql://localhost:3306"
    val driver = "com.mysql.cj.jdbc.Driver"
    val username = "root"
    val password = "Viklqs#39041"

    try {
      Class.forName(driver)
      connection = DriverManager.getConnection(url, username, password)
      //println("MySQL CONNECTION IS GOOD")

    } catch {
      case e: Exception => e.printStackTrace()
        println("An error occurred while trying to connect to SQL")
    }

  }




  def createUser(Username: String, Password: String, AdminValue: Int): Int = {
    connect()
    var resultSet = 0
    var statement = connection.prepareStatement(s"Insert into users (Username, Password, AdminValue) Values(?, ?, ?)")
    try {
      statement.setString(1, Username)
      statement.setString(2, Password)
      statement.setInt(3, AdminValue)
      resultSet = statement.executeUpdate()
      println("The user account has been created")
      //showAllUsers()
      resultSet
    }
    catch {
      case e: SQLException => e.printStackTrace()
        resultSet
    }
  }

  def showUsername(Username: String): Unit = {
    connect()
    val statement = connection.prepareStatement(s"SELECT Username FROM users WHERE Username = '$Username' ")
    try {
      val resultSet = statement.executeQuery()

      while (resultSet.next()) {
        print(resultSet.getString("Username"))
        print("\n")
      }
    } catch {
      case e: Exception => e.printStackTrace()
    }
  }

  def checkifExists(userCheck: String): Boolean = {
  connect()
    var bufferL = new ListBuffer[String]()

    val statement = connection.prepareStatement("SELECT Username FROM users")
    val resultSet = statement.executeQuery()
    while (resultSet.next()) {
      bufferL += resultSet.getString("Username")
    }
    val userList = bufferL.toList
    val check = userList.contains(userCheck)
    if (check) {//user is detected
      true
    } else {//User is not detected
      false
    }
  }

  def updateUsername(oldusername:String, NewUserName: String): Unit = {
    connect()
    val statement = connection.createStatement()
    try {
      statement.executeUpdate(s"UPDATE users SET Username = '$NewUserName' WHERE Username = '$oldusername'")
    } catch {
      case e: Exception => e.printStackTrace()
    }
    println("Username has been updated")
  }

  def updatePassword(username:String, password: String): Unit = {
    connect()
    val statement = connection.createStatement()

    try {
      statement.executeUpdate(s"UPDATE users SET password = '$password' WHERE Username = '$username'")
    } catch {
      case e: Exception => e.printStackTrace()
    }
    println("Username has been updated")

  }

  def checkLogin(usersUserName: String, usersPassword: String): Boolean = {
    connect()
    val statement = connection.prepareStatement(s"SELECT * From users WHERE Username = '$usersUserName' AND password = '$usersPassword'")
    var validUsername = statement.executeQuery()

      if (!validUsername.next()) {
        println("Username and Password are incorrect")
        //print("")
        false

      } else {
        println("Username and Password are correct.")
        //print("")
        true
      }
    
  }

  def checkAdmin(usersUserName: String): Boolean = {
    connect()

    val statement = connection.prepareStatement(s"SELECT * From users WHERE Username = '$usersUserName' AND AdminValue = '1'")
    var validUsername = statement.executeQuery()

      if (!validUsername.next()) {false}
      else {true}
  }
  

  def closeDB(): Unit = {

    connection.close()

  }
}
