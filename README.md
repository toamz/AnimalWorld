# Projekt

Simulátor zvířat pohybujících se po mřížce.
Mapa je složena z polí vody nebo trávy.

## Funkce

- Editor mapy
	- Nastavit typ polí (voda, tráva)
	- Umístit zvířata
	- Možnost načíst/uložit mapu (včetně zvířat) z vlastního typu souboru
	- Možnost načíst typ polí z obrázku (?)
- Simulátor
	- Rychlosti přehrávání:
		- Po krocích
		- Automaticky s rychlostí N kroků za vteřinu
		- Maximální rychlostí s renderováním
		- Maximální rychlostí bez renderování
	- Simulace běží v **odděleném threadu**
	- Uložení stavu simulace do souboru

## Architektura

Jelikož zvířata se mohou pohybovat pouze po mřížce, je jejich poloha uložena v objektu World.