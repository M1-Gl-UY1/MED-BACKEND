package com.example.med.repository;

import com.example.med.model.notification.Notification;
import com.example.med.model.notification.DestinataireType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NotificationRepository extends JpaRepository<Notification, Long> {

    // Notifications pour un admin (toutes les notifications ADMIN)
    @Query("SELECT n FROM Notification n WHERE n.destinataireType = 'ADMIN' ORDER BY n.dateCreation DESC")
    List<Notification> findAllForAdmin();

    // Notifications non lues pour admin
    @Query("SELECT n FROM Notification n WHERE n.destinataireType = 'ADMIN' AND n.lu = false ORDER BY n.dateCreation DESC")
    List<Notification> findUnreadForAdmin();

    // Compter les notifications non lues pour admin
    @Query("SELECT COUNT(n) FROM Notification n WHERE n.destinataireType = 'ADMIN' AND n.lu = false")
    long countUnreadForAdmin();

    // Notifications pour un utilisateur specifique (client ou societe)
    @Query("SELECT n FROM Notification n WHERE n.destinataireId = :userId AND n.destinataireType = :type ORDER BY n.dateCreation DESC")
    List<Notification> findByDestinataire(@Param("userId") Long userId, @Param("type") DestinataireType type);

    // Notifications non lues pour un utilisateur
    @Query("SELECT n FROM Notification n WHERE n.destinataireId = :userId AND n.destinataireType = :type AND n.lu = false ORDER BY n.dateCreation DESC")
    List<Notification> findUnreadByDestinataire(@Param("userId") Long userId, @Param("type") DestinataireType type);

    // Compter les notifications non lues pour un utilisateur
    @Query("SELECT COUNT(n) FROM Notification n WHERE n.destinataireId = :userId AND n.destinataireType = :type AND n.lu = false")
    long countUnreadByDestinataire(@Param("userId") Long userId, @Param("type") DestinataireType type);

    // Dernières notifications (pour le dashboard)
    List<Notification> findTop10ByDestinataireTypeOrderByDateCreationDesc(DestinataireType type);
}
