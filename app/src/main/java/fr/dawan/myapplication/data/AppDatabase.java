package fr.dawan.myapplication.data;

import androidx.room.Database;
import androidx.room.RoomDatabase;

import fr.dawan.myapplication.model.Location;

/**
 * Représentation abstraite de la base de données locale utilisant le cadriciel (framework) Room.
 * <p>
 * Gère la persistance des entités {@link Location} pour permettre un fonctionnement
 * hors-ligne (Offline-first) de l'application.
 * </p>
 */
@Database(entities = {Location.class}, version = 1)
public abstract class AppDatabase extends RoomDatabase {

    /**
     * Fournit l'objet d'accès aux données (DAO - Data Access Object) pour les centres de formation.
     *
     * @return L'instance de {@link LocationDao} permettant d'interagir avec la table des localisations.
     */
    public abstract LocationDao locationDao();
}