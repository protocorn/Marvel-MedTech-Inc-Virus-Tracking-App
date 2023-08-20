# Marvel-MedTech-Inc

Welcome to the Marvel-MedTech-Inc repository! This project represents an innovative application aimed at combating the Black Virus pandemic and providing users with essential tools to stay informed, safe, and connected. The app leverages modern technologies to empower users in a range of ways.

## Key Features

1. **Confinement Zone Detection and Alerts**: Stay informed about nearby confinement zones and receive real-time alerts to ensure safety.
2. **Black Virus Positive Alerts**: Receive immediate notifications if any Black Virus positive person is in close proximity.
3. **Interactive Map**: Explore a comprehensive map showcasing confinement zones, virus positive individuals, and vaccination centers.
4. **Personal Alert System**: Users who are virus positive and away from home will be alerted to ensure a quick response.
5. **Vaccination Center Locator**: Find the nearest vaccination center and conveniently book a vaccination slot.
6. **Online Consultation**: Users can engage in online consultations with available doctors.
7. **Self-Assessment**: Take a self-assessment test to determine your Black Virus Score and receive tailored recommendations.

## Technologies Used

### Firebase

The project effectively employs Firebase technologies for seamless authentication and data management:

- **FirebaseAuth**: Provides secure phone number-based authentication for users.
- **FirebaseFirestore**: Stores essential data, including user information, confinement zones, vaccination centers, chats, and state information.

### Geofencing

Utilizing the Geofencing API, the app offers real-time geolocation tracking and notifications when users enter confinement zones or are near virus-positive individuals. The app ensures user safety with timely alerts.

![Geofencing](https://user-images.githubusercontent.com/53559317/192082127-07d66e17-7a86-4a87-9947-7b95b62e6ed0.png)

### Maps API Integration

The project integrates Maps API to provide a global view, marked with essential locations:

- **Vaccination Centers**: Identifies nearby vaccination centers for easy access and booking.
- **Confinement Zones**: Highlights confinement zones for user awareness and avoidance.
- **Virus Positive Individuals**: Marks the presence of virus-positive individuals on the map.
- **Home Location**: Displays the user's home location.

![Maps API](https://user-images.githubusercontent.com/53559317/192082219-5bbc7ef8-aa78-4463-b497-c5b439da538c.png)

## Getting Started

To explore the capabilities of Marvel-MedTech-Inc:

1. Clone this repository: `git clone https://github.com/protocorn/Marvel-MedTech-Inc.git`
2. Refer to documentation for setup and API key integration.
3. Build and run the app on your device.

## Contributing

Contributions to this project are welcomed. If you have suggestions, enhancements, or bug fixes, feel free to open an issue or submit a pull request.

## License

This project is licensed under the [MIT License](LICENSE).
