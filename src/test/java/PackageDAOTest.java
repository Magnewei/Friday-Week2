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
    private static PackageDAO packageDao;
    private Package testPackage;

    @BeforeAll
    void setUp() {
        EntityManagerFactory emfTest = HibernateConfig.getEntityManagerFactory();
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

        Package retrievedPackage = packageDao.getById(testPackage);
        assertNotNull(retrievedPackage);
        assertEquals(retrievedPackage.getTrackingNumber(), trackingNumber);
    }


    @Test
    public void testDeletePackage() {
        String trackingNumber = "ABC123";

        Package pack = Package
                .builder()
                .deliveryStatus(Package.DeliveryStatus.PENDING)
                .senderName("Sender").
                trackingNumber(trackingNumber)
                .receiverName("Receiver").
                build();

        packageDao.create(pack);
        assertTrue(packageDao.delete(pack));
    }

    @Test
    public void testGetById() {
        assertTrue(testPackage.getId() != 0);
        assertNotNull(packageDao.getById(testPackage));
    }

    @Test
    public void testUpdatePackage() {
        testPackage.setDeliveryStatus(Package.DeliveryStatus.PENDING);
        assertTrue(packageDao.update(testPackage));

        Package pack = packageDao.getById(testPackage);
        LocalDateTime notNow = LocalDateTime.of(2021, 2, 2, 2, 2);
        assertNotSame(notNow, pack.getLastUpdated());
    }
}
