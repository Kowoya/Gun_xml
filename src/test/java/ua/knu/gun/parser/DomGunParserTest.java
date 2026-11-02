package ua.knu.gun.parser;

class DomGunParserTest extends AbstractGunParserTest {

    @Override
    protected GunParser createParser() {
        return new DomGunParser();
    }
}
