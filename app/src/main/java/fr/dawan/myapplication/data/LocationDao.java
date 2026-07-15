package fr.dawan.myapplication.data;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import java.util.List;

import fr.dawan.myapplication.model.Location;

/**
 * Data Access Object (DAO) pour la table des centres de formation.
 * Gère l'accès aux données stockées localement sur le device.
 */
@Dao
public interface LocationDao {
    /**
     * Récupère l'intégralité des centres pour l'affichage cartographique.
     * @return Liste des centres stockés en SQLite.
     */
    @Query("SELECT * FROM locations")
    List<Location> getAll();

    /**
     * Insère ou met à jour les centres récupérés depuis l'API.
     * @param locations Liste des centres à synchroniser.
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAll(List<Location> locations);
}