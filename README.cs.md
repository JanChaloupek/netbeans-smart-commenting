# Smart Commenting pro Apache NetBeans

[English](README.md) | **Čeština**

Smart Commenting je rozšíření pro Apache NetBeans, které umožňuje konfigurovatelné přepínání řádkových a blokových komentářů se zachováním správného odsazení.

Rozšíření vybírá odpovídající syntaxi komentáře podle MIME typu dokumentu a přípony souboru. Umí zpracovat aktuální řádek nebo všechny řádky dotčené aktuálním výběrem.

## Funkce

- Přepnutí komentáře aktuálního řádku nebo vybraného bloku
- Podpora řádkových i blokových komentářů
- Umístění značek komentáře podle odsazení kódu
- Automatické rozpoznání jazyka podle MIME typu a přípony
- Nastavitelný režim komentování, zarovnání a rozložení blokových značek
- Živý náhled nastavení
- Náhled vlastního kódu podle pozice kurzoru nebo označeného bloku
- Nastavitelná klávesová zkratka v Keymap NetBeans

## Podporované jazyky a typy souborů

Rozšíření aktuálně obsahuje podporu pro tyto jazyky a formáty:

- PHP
- Java
- JavaScript a TypeScript
- C, C++ a hlavičkové soubory
- C#
- Go
- Kotlin
- Rust
- Swift
- Dart
- Pascal a Delphi
- Windows Batch
- Python
- Shell skripty
- Ruby
- Perl
- Lua
- Haskell
- R
- NEON
- YAML
- TOML
- INI a CFG
- Java Properties
- Dockerfile
- SQL
- Latte
- HTML a XML
- CSS
- TeX

Pokud NetBeans ohlásí pouze obecný MIME typ, například `text/plain`, lze syntaxi komentáře dohledat také podle přípony souboru.

## Instalace

### Instalace ze souboru NBM

1. Stáhněte nejnovější soubor `.nbm` ze sekce **Releases** tohoto repozitáře.
2. Otevřete Apache NetBeans.
3. Vyberte **Tools → Plugins**.
4. Otevřete záložku **Downloaded**.
5. Klikněte na **Add Plugins...** a vyberte stažený soubor `.nbm`.
6. Dokončete instalaci a v případě výzvy restartujte NetBeans.

## Použití

Umístěte kurzor na řádek nebo označte část bloku a spusťte akci **Smart Toggle Comment**.

- Bez výběru rozšíření zpracuje řádek obsahující kurzor.
- S aktivním výběrem rozšíření rozšíří rozsah na všechny dotčené řádky a zpracuje výsledný blok.
- Pokud výběr končí přesně na začátku dalšího řádku, tento řádek se nezahrne.

Navržená výchozí klávesová zkratka:

- Windows a Linux: `Ctrl+Alt+C`
- macOS: `Command+Option+C`

Zkratku lze změnit v **Tools → Options → Keymap** vyhledáním akce **Smart Toggle Comment**.

## Nastavení

Otevřete:

**Tools → Options → Miscellaneous → Smart Commenting**

Nastavit lze:

- požadovaný režim komentování: automatický, řádkový nebo blokový,
- zarovnání řádkových komentářů,
- zarovnání blokových komentářů,
- rozložení počáteční a koncové značky blokového komentáře,
- jazyk náhledu.

Živý náhled obsahuje příklad jednoho řádku a víceřádkového výběru. Záložka **Vlastní kód** umožňuje zadat vlastní text. Bez výběru se v náhledu transformuje řádek obsahující kurzor; s výběrem se transformují všechny dotčené řádky.

## Sestavení ze zdrojových kódů

Repozitář obsahuje Ant projekt modulu pro Apache NetBeans.

1. Otevřete projekt v Apache NetBeans.
2. Zkontrolujte závislosti v **Project Properties → Libraries**.
3. Spusťte **Clean and Build**.
4. Pomocí **Create NBM** vytvořte instalovatelný balíček modulu.

Projekt aktuálně používá úroveň zdrojového kódu Java 8.

## Struktura projektu

```text
netbeans-smart-commenting/
├── src/
├── nbproject/
├── build.xml
├── manifest.mf
├── LICENSE
├── NOTICE
├── CHANGELOG.md
├── README.md
└── README.cs.md
```

Generované a lokální soubory, například `build/` a `nbproject/private/`, se nemají ukládat do repozitáře.

## Hlášení problémů

Pro hlášení problémů použijte GitHub Issues, zejména pro:

- nepodporovaný jazyk nebo příponu,
- nesprávnou syntaxi komentáře,
- problém s odsazením,
- kolizi klávesové zkratky,
- nekompatibilitu s konkrétní verzí Apache NetBeans.

K hlášení připojte verzi Apache NetBeans, operační systém, příponu souboru, zjištěný MIME typ, pokud je známý, vstupní kód a skutečný výsledek.

## Přispívání

Příspěvky jsou vítány. Před odesláním změny ověřte komentování a odkomentování pro:

- jeden řádek,
- víceřádkový výběr,
- prázdné řádky uvnitř výběru,
- mezery a tabulátory,
- poslední řádek bez ukončovacího znaku nového řádku,
- výběr končící na začátku dalšího řádku.

Užitečná podmínka testu:

```text
uncomment(comment(original)) == original
```

## Licence

Smart Commenting je poskytován pod licencí Apache License, Version 2.0.

Podrobnosti jsou uvedeny v souborech [LICENSE](LICENSE) a [NOTICE](NOTICE).

`SPDX-License-Identifier: Apache-2.0`
