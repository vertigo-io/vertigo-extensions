/**
 * vertigo - simple java starter
 *
 * Copyright (C) 2013, KleeGroup, direction.technique@kleegroup.com (http://www.kleegroup.com)
 * KleeGroup, Centre d'affaire la Boursidiere - BP 159 - 92357 Le Plessis Robinson Cedex - France
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package io.vertigo.dynamo.domain.model;

import io.vertigo.core.Home;
import io.vertigo.core.spaces.definiton.Definition;
import io.vertigo.core.spaces.definiton.DefinitionReference;
import io.vertigo.lang.Assertion;
import io.vertigo.util.StringUtil;

import java.io.Serializable;
import java.util.regex.Pattern;

/**
 * Représente l'identifiant ABSOLU d'une ressource.
 * Une ressource posséde une définition (sa classe), et une clé.
 * L'URI propose une URN, c'est é dire la transcription sous forme de chaine.
 * L'URI peut étre recomposée é partir de cette URN.
 *
 * Le générique utilisé pour caractériser l'URI dépend de la ressource et non de la définition.
 * Cela permet de créer des URI plus intuitive comme URI<Personne> qui est un identifiant de personne.
 *
 * @author  pchretien
 * @param <R> Type de la ressource représentée par l'URI
 */
public final class URI<R extends Serializable> implements Serializable {
	private static final long serialVersionUID = -1L;
	private static final char D2A_SEPARATOR = '@';

	/**
	 * Expression réguliére vérifiée par les URN.
	 */
	public static final Pattern REGEX_URN = Pattern.compile("[a-zA-Z0-9_:@$-]{5,80}");

	private final DefinitionReference<Definition> definitionRef;
	private final Serializable key;

	/** URN de la ressource (Nom complet).*/
	private final String urn;

	/**
	 * Constructeur.
	 * @param definition Definition de la ressource
	 * @param key Clé de la ressource
	 */
	public URI(final Definition definition, final Object key) {
		Assertion.checkNotNull(key);
		Assertion.checkNotNull(definition);
		//-----
		this.key = Serializable.class.cast(key);
		this.definitionRef = new DefinitionReference<>(definition);

		//Calcul de l'urn
		urn = toURN(this);
		Assertion.checkArgument(URI.REGEX_URN.matcher(urn).matches(), "urn {0} doit matcher le pattern {1}", urn, URI.REGEX_URN);
	}

	/**
	 * Il est nécessaire de passer la classe de la définition attendue.
	 *
	 * @return Définition de la ressource.
	 */
	public <D extends Definition> D getDefinition() {
		return (D) definitionRef.get();
	}

	/**
	 * Récupére l'URN é partir de l'URI.
	 * Une URN est la  représentation unique d'une URI sous forme de chaine de caractéres.
	 * Cette chaine peut s'insérer telle que dans une URL en tant que paramétre
	 * et ne contient donc aucun caractére spécial.
	 * Une URN respecte la regex exprimée ci dessus.
	 * @return URN de la ressource.
	 */
	public String toURN() {
		return urn;
	}

	/**
	 * @return Clé identifiant la ressource parmi les ressources du méme type.
	 * Exemple : identifiant numérique d'une commande.
	 */
	public Serializable getKey() {
		return key;
	}

	//=========================================================================
	//=============================OVERRIDE====================================
	//=========================================================================

	/** {@inheritDoc} */
	@Override
	public int hashCode() {
		return toURN().hashCode();
	}

	/** {@inheritDoc} */
	@Override
	public boolean equals(final Object o) {
		if (o instanceof URI) {
			return ((URI) o).toURN().equals(this.toURN());
		}
		return false;
	}

	/** {@inheritDoc} */
	@Override
	public String toString() {
		//on surcharge le toString car il est utilisé dans les logs d'erreur. et celui par défaut utilise le hashcode.
		return "urn[" + getClass().getName() + "]::" + toURN();
	}

	//=========================================================================
	//=============================STATIC======================================
	//=========================================================================

	public static URI<?> fromURN(final String urn) {
		Assertion.checkNotNull(urn);
		//-----
		final int i = urn.indexOf(D2A_SEPARATOR);
		final String dname = urn.substring(0, i);
		final Object key = stringToKey(urn.substring(i + 1));

		//On ne type pas, la seule chose que l'on sait est qu'il s'agit d'une définition.
		final Definition definition = Home.getDefinitionSpace().resolve(dname);
		return new URI(definition, key);
	}

	private static String toURN(final URI<?> uri) {
		final String keyAsText = keyToString(uri.getKey());
		return uri.getDefinition().getName() + D2A_SEPARATOR + keyAsText;
	}

	/**
	 * Converti une clé en chaine.
	 * Une clé vide est considérée comme nulle.
	 * @param key clé
	 * @return Chaine représentant la clé
	 */
	private static String keyToString(final Serializable key) {
		if (key == null) {
			return null;
		}
		if (key instanceof String) {
			return StringUtil.isEmpty((String) key) ? null : "s-" + ((String) key).trim();
		} else if (key instanceof Integer) {
			return "i-" + key;
		} else if (key instanceof Long) {
			return "l-" + key;
		}
		throw new IllegalArgumentException(key.toString() + " n'est pas géré par URI");
	}

	/**
	 * Converti une chaine en clé.
	 * @param strValue Valeur
	 * @return Clé lue é partir de la chaine
	 */
	private static Serializable stringToKey(final String strValue) {
		if (StringUtil.isEmpty(strValue)) {
			return null;
		}
		if (strValue.startsWith("s-")) {
			return strValue.substring(2);
		} else if (strValue.startsWith("i-")) {
			return Integer.valueOf(strValue.substring(2));
		} else if (strValue.startsWith("l-")) {
			return Long.valueOf(strValue.substring(2));
		}
		throw new IllegalArgumentException(strValue + " n'est pas géré par par URI.");
	}
}
