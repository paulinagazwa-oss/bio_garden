package com.github.paulinagazwa.oss.bio.garden.entity;

/**
 * Typ relacji między roślinami w ogrodzie
 */
public enum RelationshipType {
	/**
	 * Dobry sąsiad - rośliny korzystnie na siebie wpływają
	 */
	GOOD,

	/**
	 * Zły sąsiad - rośliny negatywnie na siebie wpływają
	 */
	BAD,

	/**
	 * Obojętny sąsiad - rośliny nie wpływają na siebie
	 */
	NEUTRAL,

	/**
	 * Rośliny mogące rosnąć współrzędnie (w tym samym rzędzie)
	 */
	COMPANION_ROW
}

