# Marvel-MedTech-Inc

**Key Features**
1. It detects and alerts user whenever the user enters any **confinement zone**
2. It sends an alert if any **Black Virus positive** person is nearby
3. Shows user the entire map where the user can find **confinemnet zones, virus positive people, and vaccination centres**
4. Alerts user, if the user is **virus positive** and **away from home.**
5. Allows user to find a **vaccination centre** and **book a slot**.
6. The user can seek **online consultation** with any of the doctors who are **currently active** on the app.
7. The users can give a self-assessment test wherin they can find their **Black Virus Score** and depending on that, it shows which measures the user must take.

# Technologies Used

**1.Firebase**

This project uses ***FirebaseAuth*** and ***FirebaseFirestore*** for **Authentication** and **Data Storage** respectively. The user can login by entering their **phone number**.

The *database* stores :
1. **User's information** - Phone number, userId, self-assessment score, current location and home location
2. **Confinemnt zones** - Latitude, Longitude and radius
3. **Vaccination Centres** - Latitude, Longitude and name
4. **chats** - message, time, timestamp, userId
5. **States** - danger level, active, recovered, deceased

**2.Geofencing**

The app uses **Geofencing API** to detect whenever the user enters any confinement zone or any virus positive person is nearby. The app has the location access in the background
so that whenever user enters a **Geofence** it will get an alert notification.

**3.Maps API**

The *Maps API* is used to show entire world map with markers of vaccination centers, confinemnet zones and virus positive person. It is also used for marking the home location of the user.


