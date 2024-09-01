import app.persistence.DAOs.PackageDAO;
import app.persistence.HibernateConfig;
import app.persistence.entities.Package;
import jakarta.persistence.EntityManagerFactory;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class PackageDAOTest {
    private static EntityManagerFactory emfTest;
    private static PackageDAO packageDao;
    private Package testPackage;

    @BeforeAll
    void setUp() {
        emfTest = HibernateConfig.getEntityManagerFactory();
        packageDao = new PackageDAO(emfTest);

        testPackage = Package
                .builder()
                .deliveryStatus(Package.DeliveryStatus.PENDING)
                .senderName("Sender")
                .trackingNumber("ABC123")
                .receiverName("Receiver")
                .build();

        packageDao.create(testPackage);
    }

    @AfterAll
    public void tearDown() {
        if (testPackage != null) packageDao.delete(testPackage);
    }


    @Test
    public void testPersistPackage() {
        String trackingNumber = "ABC123";
        long packageId = testPackage.getId();

        Package retrievedPackage = packageDao.getById(packageId);
        assertNotNull(retrievedPackage);
        assertEquals(retrievedPackage.getTrackingNumber(), trackingNumber);
    }


    @Test
    public void testDeletePackage() {
        String trackingNumber = "ABC123";

        Package pack = Package
                .builder()
                .deliveryStatus(Package.DeliveryStatus.PENDING)
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
        testPackage.setDeliveryStatus(Package.DeliveryStatus.PENDING);

        boolean updateSuccessful = packageDao.update(testPackage);
        assertTrue(updateSuccessful);

        Package pack = packageDao.getById(id);
        LocalDateTime notNow = LocalDateTime.of(2021, 2, 2, 2, 2);
        assertNotSame(notNow, pack.getLastUpdated());
    }
}
