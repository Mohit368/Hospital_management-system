
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

// Patient class
class Patient {
    private String name;
    private int age;
    private String id;

    public Patient(String name, int age, String id) {
        this.name = name;
        this.age = age;
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public String getId() {
        return id;
    }

    @Override
    public String toString() {
        return "Patient ID: " + id + ", Name: " + name + ", Age: " + age;
    }
}

// Doctor class
class Doctor {
    private String name;
    private String specialization;
    private String id;

    public Doctor(String name, String specialization, String id) {
        this.name = name;
        this.specialization = specialization;
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public String getSpecialization() {
        return specialization;
    }

    public String getId() {
        return id;
    }

    @Override
    public String toString() {
        return "Doctor ID: " + id + ", Name: " + name + ", Specialization: " + specialization;
    }
}

// Appointment class
class Appointment {
    private Patient patient;
    private Doctor doctor;
    private String date;
    private String time;

    public Appointment(Patient patient, Doctor doctor, String date, String time) {
        this.patient = patient;
        this.doctor = doctor;
        this.date = date;
        this.time = time;
    }

    public Patient getPatient() {
        return patient;
    }

    public Doctor getDoctor() {
        return doctor;
    }

    public String getDate() {
        return date;
    }

    public String getTime() {
        return time;
    }

    @Override
    public String toString() {
        return "Appointment - Patient: " + patient.getName() + ", Doctor: " + doctor.getName() + 
               ", Date: " + date + ", Time: " + time;
    }
}

// Main class
public class HospitalManagementSystem {
    private static List<Patient> patients = new ArrayList<>();
    private static List<Doctor> doctors = new ArrayList<>();
    private static List<Appointment> appointments = new ArrayList<>();
    private static int patientCount = 1; // To generate unique patient IDs

    private static String[] appointmentTimes = {
        "09:00", "10:00", "11:00", "12:00", "13:00", 
        "14:00", "15:00", "16:00", "17:00", "18:00"
    };

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        // Adding 15 doctors with specified names and specializations
        String[] doctorNames = {
            "Dr. Dhruv Saxena", "Dr. Jan Sahu", "Dr. Pragya Srivastav", "Dr. Komal Jain", "Dr. Satyam Singh",
            "Dr. Sachin Rastogi", "Dr. Raj Ahuja", "Dr. HariOm Das", "Dr. Mohit Kumar", "Dr. Rahul Patel",
            "Dr. Suman Sharma", "Dr. Naina Gupta", "Dr. Firoz Khan", "Dr. Baljinder Kaur", "Dr. Neha Agarwal"
        };

        String[] specializations = {
            "Cardiology", "Neurology", "Pediatrics", "Dermatology", "Oncology",
            "Radiology", "Anesthesiology", "Endocrinology", "Gastroenterology", "Psychiatry",
            "Urology", "Pulmonology", "Rheumatology", "Emergency Medicine", "Family Medicine"
        };

        for (int i = 0; i < 15; i++) {
            doctors.add(new Doctor(doctorNames[i], specializations[i % specializations.length], "D" + String.format("%03d", i + 1)));
        }

        // Adding 8 patients with specific names
        String[] patientNames = {
            "Alice", "Bob", "Charlie", "David", "Eva", 
            "Frank", "Grace", "Hannah"
        };

        for (int i = 0; i < 8; i++) {
            patients.add(new Patient(patientNames[i], 20 + (i % 30), "P" + String.format("%03d", i + 1)));
        }

        // User interaction
        while (true) {
            System.out.println("\nHospital Management System");
            System.out.println("1. View Patients");
            System.out.println("2. View Doctors");
            System.out.println("3. Schedule Appointment");
            System.out.println("4. View Appointments");
            System.out.println("5. Add Patient");
            System.out.println("6. Search Patient by Name");
            System.out.println("7. Search Doctor by Name");
            System.out.println("8. Exit");
            System.out.print("Choose an option: ");
            String input = scanner.nextLine();

            try {
                int choice = Integer.parseInt(input);
                switch (choice) {
                    case 1:
                        System.out.println("\nPatients:");
                        for (Patient patient : patients) {
                            System.out.println(patient);
                        }
                        break;
                    case 2:
                        System.out.println("\nDoctors:");
                        for (Doctor doctor : doctors) {
                            System.out.println(doctor);
                        }
                        break;
                    case 3:
                        scheduleAppointment(scanner);
                        break;
                    case 4:
                        System.out.println("\nAppointments:");
                        for (Appointment appointment : appointments) {
                            System.out.println(appointment);
                        }
                        break;
                    case 5:
                        addPatient(scanner);
                        break;
                    case 6:
                        searchPatientByName(scanner);
                        break;
                    case 7:
                        searchDoctorByName(scanner);
                        break;
                    case 8:
                        System.out.println("Exiting...");
                        scanner.close();
                        return;
                    default:
                        System.out.println("Invalid option. Please try again.");
                }
            } catch (NumberFormatException e) {
                System.out.println("Please enter a valid number.");
            }
        }
    }

    private static void scheduleAppointment(Scanner scanner) {
        System.out.print("Enter Patient ID: ");
        String patientId = scanner.nextLine();
        Patient selectedPatient = findPatientById(patientId);
        if (selectedPatient != null) {
            System.out.print("Enter Doctor ID: ");
            String doctorId = scanner.nextLine();
            Doctor selectedDoctor = findDoctorById(doctorId);
            if (selectedDoctor != null) {
                System.out.print("Enter Appointment Date (YYYY-MM-DD): ");
                String date = scanner.nextLine();
                
                // Automatically select a time for the appointment
                String time = appointmentTimes[appointments.size() % appointmentTimes.length];

                // Check if the appointment slot is available
                if (isSlotAvailable(date, time)) {
                    Appointment appointment = new Appointment(selectedPatient, selectedDoctor, date, time);
                    appointments.add(appointment);
                    System.out.println("Appointment scheduled successfully.");
                    showAppointmentSlip(appointment);
                } else {
                    System.out.println("Error: The appointment slot on " + date + " at " + time + " is not available.");
                }
            } else {
                System.out.println("Doctor not found.");
            }
        } else {
            System.out.println("Patient not found.");
        }
    }

    private static boolean isSlotAvailable(String date, String time) {
        for (Appointment appointment : appointments) {
            if (appointment.getDate().equals(date) && appointment.getTime().equals(time)) {
                return false; // Slot is already booked
            }
        }
        return true; // Slot is available
    }

    private static void addPatient(Scanner scanner) {
        System.out.print("Enter Patient Name: ");
        String name = scanner.nextLine();
        System.out.print("Enter Patient Age: ");
        int age = scanner.nextInt();
        scanner.nextLine(); // consume newline
        String id = "P" + String.format("%03d", patientCount++); // Create a unique ID like P001, P002, etc.
        patients.add(new Patient(name, age, id));
        System.out.println("Patient added successfully with ID: " + id);
    }

    private static void showAppointmentSlip(Appointment appointment) {
        System.out.println("\n--- Appointment Slip ---");
        System.out.println("Patient Name: " + appointment.getPatient().getName());
        System.out.println("Doctor Name: " + appointment.getDoctor().getName());
        System.out.println("Doctor Specialization: " + appointment.getDoctor().getSpecialization());
        System.out.println("Appointment Date: " + appointment.getDate());
        System.out.println("Appointment Time: " + appointment.getTime());
        System.out.println("-------------------------");
    }

    private static Patient findPatientById(String id) {
        for (Patient patient : patients) {
            if (patient.getId().equals(id)) {
                return patient;
            }
        }
        return null;
    }

    private static Doctor findDoctorById(String id) {
        for (Doctor doctor : doctors) {
            if (doctor.getId().equals(id)) {
                return doctor;
            }
        }
        return null;
    }

    private static void searchPatientByName(Scanner scanner) {
        System.out.print("Enter Patient Name to search: ");
        String name = scanner.nextLine();
        boolean found = false;
        for (Patient patient : patients) {
            if (patient.getName().equalsIgnoreCase(name)) {
                System.out.println(patient);
                found = true;
            }
        }
        if (!found) {
            System.out.println("No patient found with the name: " + name);
        }
    }

    private static void searchDoctorByName(Scanner scanner) {
        System.out.print("Enter Doctor Name to search: ");
        String name = scanner.nextLine();
        boolean found = false;
        for (Doctor doctor : doctors) {
            if (doctor.getName().equalsIgnoreCase(name)) {
                System.out.println(doctor);
                found = true;
            }
        }
        if (!found) {
            System.out.println("No doctor found with the name: " + name);
        }
    }
}
