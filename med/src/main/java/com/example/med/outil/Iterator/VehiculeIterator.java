package com.example.med.outil.Iterator;

/**
 * PATTERN ITERATOR - Iterator (Interface)
 *
 * Interface définissant les opérations pour parcourir une collection.
 * Suit le protocole standard de l'itérateur: hasNext() et next().
 */
public interface VehiculeIterator {

    /**
     * Vérifie s'il reste des éléments à parcourir
     * @return true s'il y a encore des éléments
     */
    boolean hasNext();

    /**
     * Retourne l'élément suivant et avance le curseur
     * @return L'élément suivant dans la collection
     */
    Object next();
}
