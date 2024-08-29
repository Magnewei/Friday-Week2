import app.Persistence.DAOs.PackageDAO;
import app.Persistence.Entities.DeliveryStatus;
import app.Persistence.Entities.Package;
import app.Persistence.HibernateConfig;
import jakarta.persistence.EntityManagerFactory;
import org.junit.jupiter.api.*;

import static org.junit.jupiter.api.Assertions.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class PackageDAOTest {
    private static EntityManagerFactory emfTest = HibernateConfig.getEntityManagerFactory();;
    private static final PackageDAO packageDao = new PackageDAO(emfTest);
    private Package testPackage;

    @BeforeEach
    void setUp() {

        // Create and persist a new package before each test
        testPackage = Package.builder()
                .deliveryStatus(DeliveryStatus.PENDING)
                .senderName("Sender")
                .trackingNumber("ABC123")
                .receiverName("Receiver")
                .build();

        // Persist the package to be used in each test
        packageDao.create(testPackage);
    }


    @AfterEach
    public void tearDownEach() {
        // Delete the package after each test to clean up
        if (testPackage != null) packageDao.delete(testPackage);
    }


    @Test
    public void testPersistPackage() {
        String trackingNumber = "ABC123";
        long packageId = testPackage.getId();

        Package retrievedPackage = emfTest.createEntityManager().find(Package.class, packageId);
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
