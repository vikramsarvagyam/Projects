import pandas as pd
import mysql.connector as mysql
from mysql.connector import Error

table1 = pd.read_csv('C:\\Users\\vikra\\Documents\\REVATURE-TRAINING\\Project0-table1.csv', index_col=False, delimiter = ',')
table1.head()
table2 = pd.read_csv('C:\\Users\\vikra\\Documents\\REVATURE-TRAINING\\Project0-table2.csv', index_col=False, delimiter = ',')
table2.head()
table3 = pd.read_csv('C:\\Users\\vikra\\Documents\\REVATURE-TRAINING\\Project0-table3.csv', index_col=False, delimiter = ',')
table3.head()

def menu():
    print("Options: Enter any of 1-4. Press 5 to exit")
    print("1) read and display")
    print("2) update")
    print("3) delete")
    print("4) insert")
    print("5) exit")

def action(number):
    if number == 1:
        read_and_display()
    elif number == 2:  
        update()
    elif number == 3:
        delete()  
    elif number == 4:
        insert()
    else:
        print("Only numbers 1, 2, 3, 4 are valid options. Press 5 to quit")

def create_db():
    try:
        conn = mysql.connect(host='localhost',  user='root', password='Viklqs#39041')
        if conn.is_connected():
            cursor = conn.cursor()
            cursor.execute("DROP DATABASE IF EXISTS School")
            cursor.execute("CREATE DATABASE School")
            print("Database is created")
    except Error as e:
        print("Error while connecting to MySQL:", e)

def create_tables():
    try:
        conn = mysql.connect(host='localhost', database='school', user='root', password='Viklqs#39041')
        if conn.is_connected():
            cursor = conn.cursor()
            cursor.execute("select database();")
            record = cursor.fetchone()
            print("You're connected to database: ", record)

            cursor.execute('DROP TABLE IF EXISTS student;')
            cursor.execute("CREATE TABLE student(studentID int NOT NULL, Name varchar(255) NOT NULL, Age int NOT NULL, Grade int NOT NULL, PRIMARY KEY (studentID))")
            print("Table is created....")
            for i,row in table1.iterrows(): 
                sql = "INSERT INTO school.student VALUES (%s,%s,%s,%s)"
                cursor.execute(sql, tuple(row))
                conn.commit()
            print("Records inserted")

            cursor.execute('DROP TABLE IF EXISTS marks;')
            cursor.execute("CREATE TABLE marks(studentID int NOT NULL, Math int NOT NULL, Science int NOT NULL, Social_Studies int NOT NULL, FOREIGN KEY (studentID) REFERENCES student(studentID))")
            print("Table is created....")
            for i,row in table2.iterrows(): 
                sql = "INSERT INTO school.marks VALUES (%s,%s,%s,%s)"
                cursor.execute(sql, tuple(row))
                conn.commit()
            print("Records inserted")

            cursor.execute('DROP TABLE IF EXISTS tutor;')
            cursor.execute("CREATE TABLE tutor(tutorID int NOT NULL, Name varchar(255) NOT NULL, Subject varchar(255) NOT NULL, Rating int NOT NULL, PRIMARY KEY (tutorID))")
            print("Table is created....")
            for i,row in table3.iterrows():
                sql = "INSERT INTO school.tutor VALUES (%s,%s,%s,%s)"
                cursor.execute(sql, tuple(row))
                conn.commit()
            print("Records inserted")

    except Error as e:
        print("Error connecting to MySQL:", e)

def read_and_display():
    try:
        conn = mysql.connect(host='localhost', database='school', user='root', password='Viklqs#39041')
        if conn.is_connected():
            cursor = conn.cursor()
            table = input("Enter table from which you want the query from: ")
            select_query = input("Enter query: ")
            cursor.execute(select_query)
            records = cursor.fetchall()
            print("\nTotal number of rows in table: ", cursor.rowcount, "\n")
            if(table == "student"):
                for row in records:
                    print("ID -", row[0])
                    print("Name -", row[1])
                    print("Age -", row[2])
                    print("Grade -", row[3], "\n")
            elif(table == "marks"):
                for row in records:
                    print("ID -", row[0])
                    print("Math -", row[1], "/ 100")
                    print("Science -", row[2], "/ 100")
                    print("Social Studies -", row[3], "/ 100", "\n")
            elif(table == "tutor"):
                for row in records:
                    print("ID -", row[0])
                    print("Name -", row[1])
                    print("Subject -", row[2])
                    print("Rating -", row[3], "\n")

    except Error as e:
        print("Error connecting to MySQL:", e)

def update():
    try:
        conn = mysql.connect(host='localhost', database='school', user='root', password='Viklqs#39041')
        if conn.is_connected():
            cursor = conn.cursor()
            update_query = input("Enter query: ")
            cursor.execute(update_query)
            conn.commit()
            print("\nRecord in table successfully updated. \n")
    except Error as e:
        print("Error connecting to MySQL:", e)

def delete():
    try:
        conn = mysql.connect(host='localhost', database='school', user='root', password='Viklqs#39041')
        if conn.is_connected():
            cursor = conn.cursor()
            delete_query = input("Enter query: ")
            cursor.execute(delete_query)
            conn.commit()
            print("\nRecord(s) in table successfully deleted. \n")

    except Error as e:
        print("Error connecting to MySQL:", e)

def insert():
    try:
        conn = mysql.connect(host='localhost', database='school', user='root', password='Viklqs#39041')
        if conn.is_connected():
            cursor = conn.cursor()
            insert_query = input("Enter query: ")
            cursor.execute(insert_query)
            conn.commit()
            print("\nRecord in table successfully inserted. \n")

    except Error as e:
        print("Error connecting to MySQL:", e)

def main():
    create_db()
    create_tables()
    menu()
    while True:
        try:
            num = int(input("Enter your option: "))
            break
        except:
            print("Not a valid input. Try again")

    while(num != 5):
        action(num)
        print("")
        menu()
        print("")
        while True:
            try:
                num = int(input("Enter your next option: "))
                break
            except:
                print("Not a valid input. Try again")

if __name__ == "__main__":
    main()