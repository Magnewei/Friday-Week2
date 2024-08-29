import app.Persistence.DAOs.PackageDAO;
import app.Persistence.Entities.DeliveryStatus;
import app.Persistence.Entities.Package;
import app.Persistence.HibernateConfig;
import jakarta.persistence.EntityManagerFactory;
import org.junit.jupiter.api.*;

import static org.junit.jupiter.api.Assertions.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class TestExample {
    private static EntityManagerFactory emfTest = HibernateConfig.getEntityManagerFactory();;
    private static final PackageDAO packageDao = new PackageDAO(emfTest);
    private Package testPackage;

    @BeforeAll
    static void setUpAll() {
        emfTest = HibernateConfig.getEntityManagerFactory();
    }

    @BeforeEach
    void setUp() {
        // Create and persist a new package before each test
        testPackage = Package.builder()
                .deliveryStatus(DeliveryStatus.PENDING)
                .senderName("Sender")
                .trackingNumber("ABC123")
                .receiverName("Receiver")
                .build();

        packageDao.create(testPackage); // Persist the package to be used in each test
    }

    @AfterAll
    public static void tearDown() {
        if (emfTest.createEntityManager().getTransaction().isActive()) {
            emfTest.createEntityManager().getTransaction().rollback();
        }

        emfTest.createEntityManager().close();
    }

    @AfterEach
    public void tearDownEach() {
        // Delete the package after each test to clean up

        if (testPackage != null) {
            packageDao.delete(testPackage);
        }
    }


    @Test
    public void testPersistPackage() {
        String trackingNumber = "ABC123";
        assertNotNull(testPackage);

        Package retrievedPackage = emfTest.createEntityManager().find(Package.class, testPackage.getId());
        assertNotNull(retrievedPackage);
        assertEquals(retrievedPackage.getTrackingNumber(), trackingNumber);
    }


    @Test
    public void testDeletePackage() {
        String trackingNumber = "ABC123";

        Package pack = Package.builder()
                .deliveryStatus(DeliveryStatus.PENDING)
                .senderName("Sender").trackingNumber(trackingNumber)
                .receiverName("Receiver").build();

        packageDao.create(pack);

        boolean successfulDeletion = packageDao.delete(pack);
        assertTrue(successfulDeletion);
    }

    @Test
    public void testGetById() {
        long id = testPackage.getId();
        assertNotNull(packageDao.getById(id));
    }

    @Test
    public void testUpdatePackage() {
        long id = testPackage.getId();

        // Iterate the enum's delivery status.
        DeliveryStatus deliveryStatus = testPackage.getDeliveryStatus().getNextStatus();
        testPackage.setDeliveryStatus(deliveryStatus);

        // Update the package in the database.
        boolean updateSuccessful = packageDao.update(testPackage);
        assertTrue(updateSuccessful);

        // Assert that the delivery status has been updated in the database.
        Package pack = packageDao.getById(id);
        Assertions.assertNotSame(DeliveryStatus.PENDING, pack.getDeliveryStatus());
    }
}
