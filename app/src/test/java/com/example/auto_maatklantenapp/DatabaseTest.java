package com.example.auto_maatklantenapp;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import android.content.Context;

import androidx.room.Room;
import androidx.test.core.app.ApplicationProvider;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.example.auto_maatklantenapp.classes.Car;
import com.example.auto_maatklantenapp.classes.Customer;
import com.example.auto_maatklantenapp.classes.Rental;
import com.example.auto_maatklantenapp.dao.CarDao;
import com.example.auto_maatklantenapp.dao.CustomerDao;
import com.example.auto_maatklantenapp.dao.RentalDao;
import com.example.auto_maatklantenapp.database.AutoMaatDatabase;
import com.example.auto_maatklantenapp.helper_classes.RentalState;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.ArrayList;
import java.util.List;

@RunWith(AndroidJUnit4.class)
public class DatabaseTest {
    private CarDao carDao;
    private CustomerDao customerDao;
    private RentalDao rentalDao;
    private AutoMaatDatabase db;

    @Before
    public void createDb() {
        Context context = ApplicationProvider.getApplicationContext();
        db = Room.inMemoryDatabaseBuilder(context, AutoMaatDatabase.class).allowMainThreadQueries().build();
        carDao = db.carDao();
        customerDao = db.customerDao();
        rentalDao = db.rentalDao();
    }

    @After
    public void closeDb() {
        db.close();
    }

    @Test
    public void writeAndReadCar() {
        ArrayList<Car> allCars = new ArrayList<>();
        allCars.add(new Car(1,"Brand1", "Model1", "DIESEL", "No options",
                "ABC123", 1, 2024, "Since1", 100,
                4, "SEDAN", 202555.20F, 101233.10F));
        allCars.add(new Car(2,"Brand2", "Model2", "OIL", "Sunroof",
                "DEF456", 1, 2024, "Since1", 90,
                4, "TRUCK", 214240.23F, 2513523.89F));
        allCars.add(new Car(3,"Brand3", "Model3", "ELECTRIC", "Seatwarmer",
                "GHI", 1, 2024, "Since1", 125,
                4, "SEDAN", 4723508.21F, 707073.98F));
        allCars.add(new Car(4,"Brand4", "Model4", "DIESEL", "Kid safety",
                "JKL", 1, 2024, "Since1", 250,
                6, "SUV", 21251.67F, 111278.65F));

        carDao.insertAll(allCars);
        List<Car> returnedCarList = carDao.getAll();

        assertEquals(allCars.size(), returnedCarList.size());

        assertEquals(allCars.get(0).getBrand(), returnedCarList.get(0).getBrand());
        assertEquals(allCars.get(1).getBrand(), returnedCarList.get(1).getBrand());
        assertEquals(allCars.get(2).getBrand(), returnedCarList.get(2).getBrand());
        assertEquals(allCars.get(3).getBrand(), returnedCarList.get(3).getBrand());

        assertEquals(allCars.get(0).getOptions(), returnedCarList.get(0).getOptions());
        assertEquals(allCars.get(1).getOptions(), returnedCarList.get(1).getOptions());
        assertEquals(allCars.get(2).getOptions(), returnedCarList.get(2).getOptions());
        assertEquals(allCars.get(3).getOptions(), returnedCarList.get(3).getOptions());

        assertEquals(allCars.get(0).getPrice(), returnedCarList.get(0).getPrice());
        assertEquals(allCars.get(1).getPrice(), returnedCarList.get(1).getPrice());
        assertEquals(allCars.get(2).getPrice(), returnedCarList.get(2).getPrice());
        assertEquals(allCars.get(3).getPrice(), returnedCarList.get(3).getPrice());
    }

    @Test
    public void resetCarTable() {
        //Fill and delete original car list.
        Car car1 = new Car(1,"Brand1", "Model1", "DIESEL", "No options",
                "ABC123", 1, 2024, "Since1", 100,
                4, "SEDAN", 202555.20F, 101233.10F);
        Car car2 = new Car(2,"Brand2", "Model2", "OIL", "Sunroof",
                "DEF456", 1, 2024, "Since1", 90,
                4, "TRUCK", 214240.23F, 2513523.89F);
        ArrayList<Car> originalAllCars = new ArrayList<>();
        originalAllCars.add(car1);
        originalAllCars.add(car2);

        carDao.insertAll(originalAllCars);
        List<Car> returnedCarList = carDao.getAll();

        assertEquals(originalAllCars.size(), returnedCarList.size());

        assertEquals(originalAllCars.get(0).getBrand(), returnedCarList.get(0).getBrand());
        assertEquals(originalAllCars.get(1).getBrand(), returnedCarList.get(1).getBrand());

        assertEquals(originalAllCars.get(0).getOptions(), returnedCarList.get(0).getOptions());
        assertEquals(originalAllCars.get(1).getOptions(), returnedCarList.get(1).getOptions());

        assertEquals(originalAllCars.get(0).getPrice(), returnedCarList.get(0).getPrice());
        assertEquals(originalAllCars.get(1).getPrice(), returnedCarList.get(1).getPrice());

        carDao.deleteAll();

        // Fill with new car list
        ArrayList<Car> newAllCars = new ArrayList<>();
        newAllCars.add(car2);

        carDao.insertAll(newAllCars);
        List<Car> newReturnedCarList = carDao.getAll();

        assertEquals(newAllCars.size(), newReturnedCarList.size());
    }

    @Test
    public void writeAndReadCustomer() {
        Customer customer = new Customer();
        customer.setId(1500);
        customer.setSystemId(1050);
        customer.setNr(50);
        customer.setLogin("user");
        customer.setPassword("password");
        customer.setFirstName("first name");
        customer.setLastName("last name");
        customer.setEmail("email@goodle.dom");
        customer.setPersistence(false);
        customer.setAuthToken("token");

        customerDao.insertCustomer(customer);
        Customer returnedCustomer = customerDao.getFirstCustomer();
        assertEquals(customer.getId(), returnedCustomer.getId());
        assertEquals(customer.getSystemId(), returnedCustomer.getSystemId());
        assertEquals(customer.getLogin(), returnedCustomer.getLogin());
        assertEquals(customer.getPassword(), returnedCustomer.getPassword());
        assertEquals(customer.getPersistence(), returnedCustomer.getPersistence());
        assertEquals(customer.getAuthToken(), returnedCustomer.getAuthToken());
    }

    @Test
    public void readNonExistantCustomer() {
        Customer returnedCustomer = customerDao.getFirstCustomer();
        assertNull(returnedCustomer);
    }

    @Test
    public void writeAndReadRental() {
        ArrayList<Rental> allRentals = new ArrayList<>();
        allRentals.add(new Rental(1, "001", 2352352.35, 231212.71,
                "2024-01-01", "2024-02-01", RentalState.ACTIVE, 1, 1));
        allRentals.add(new Rental(4, "013", 2352352.35, 231212.71,
                "2024-01-01", "2024-02-01", RentalState.ACTIVE, 3, 1));
        allRentals.add(new Rental(8, "023", 2352352.35, 231212.71,
                "2024-01-01", "2024-02-01", RentalState.ACTIVE, 4, 1));
        allRentals.add(new Rental(9, "0045", 2352352.35, 231212.71,
                "2024-01-01", "2024-02-01", RentalState.ACTIVE, 7, 1));

        rentalDao.insertAll(allRentals);
        List<Rental> returnedRentalList = rentalDao.getAll();

        assertEquals(allRentals.size(), returnedRentalList.size());

        assertEquals(allRentals.get(0).getUid(), returnedRentalList.get(0).getUid());
        assertEquals(allRentals.get(1).getUid(), returnedRentalList.get(1).getUid());
        assertEquals(allRentals.get(2).getUid(), returnedRentalList.get(2).getUid());
        assertEquals(allRentals.get(3).getUid(), returnedRentalList.get(3).getUid());

        assertEquals(allRentals.get(0).getCode(), returnedRentalList.get(0).getCode());
        assertEquals(allRentals.get(1).getCode(), returnedRentalList.get(1).getCode());
        assertEquals(allRentals.get(2).getCode(), returnedRentalList.get(2).getCode());
        assertEquals(allRentals.get(3).getCode(), returnedRentalList.get(3).getCode());

        assertEquals(allRentals.get(0).getCarId(), returnedRentalList.get(0).getCarId());
        assertEquals(allRentals.get(1).getCarId(), returnedRentalList.get(1).getCarId());
        assertEquals(allRentals.get(2).getCarId(), returnedRentalList.get(2).getCarId());
        assertEquals(allRentals.get(3).getCarId(), returnedRentalList.get(3).getCarId());

        assertEquals(allRentals.get(0).getCustomerId(), returnedRentalList.get(0).getCustomerId());
        assertEquals(allRentals.get(1).getCustomerId(), returnedRentalList.get(1).getCustomerId());
        assertEquals(allRentals.get(2).getCustomerId(), returnedRentalList.get(2).getCustomerId());
        assertEquals(allRentals.get(3).getCustomerId(), returnedRentalList.get(3).getCustomerId());
    }

    @Test
    public void readNonExistantRental() {
        Rental rental1 = new Rental(1, "001", 2352352.35, 231212.71,
                "2024-01-01", "2024-02-01", RentalState.ACTIVE, 1, 1);
        Rental rental2 = new Rental(4, "013", 2352352.35, 231212.71,
                "2024-01-01", "2024-02-01", RentalState.ACTIVE, 3, 1);

        ArrayList<Rental> allRentals = new ArrayList<>();
        allRentals.add(rental1);
        allRentals.add(rental2);

        rentalDao.insertAll(allRentals);
        rentalDao.delete(rental1);

        int[] id = {4};

        List<Rental> rentals = rentalDao.loadAllByIds(id);
        assertEquals(rentals.get(0).getUid(), rental2.getUid());
    }
}
