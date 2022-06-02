package ca.bc.gov.educ.api.institute.service.v1;

import ca.bc.gov.educ.api.institute.exception.InstituteAPIRuntimeException;
import lombok.extern.slf4j.Slf4j;
import lombok.val;

import javax.persistence.EntityManagerFactory;

@Slf4j
public abstract class BaseService {
  protected final EntityManagerFactory emf;

  protected BaseService(EntityManagerFactory emf) {
    this.emf = emf;
  }

  protected int execUpdate(final String updateStatement) {
    val em = this.emf.createEntityManager();

    val tx = em.getTransaction();

    var rowsUpdated = 0;
    // below timeout is in milli seconds, so it is 10 seconds.
    try {
      tx.begin();
      log.info("generated sql is :: {}", updateStatement);
      final var nativeQuery = em.createNativeQuery(updateStatement).setHint("javax.persistence.query.timeout", 10000);
      rowsUpdated = nativeQuery.executeUpdate();
      tx.commit();
    } catch (final Exception e) {
      log.error("Error occurred saving entity " + e.getMessage());
      throw new InstituteAPIRuntimeException("Error occurred saving entity", e);
    } finally {
      if (em.isOpen()) {
        em.close();
      }
    }
    return rowsUpdated;
  }
}
