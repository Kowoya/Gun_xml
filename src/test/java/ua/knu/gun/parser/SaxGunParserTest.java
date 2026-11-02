package ua.knu.gun.parser;

class SaxGunParserTest extends AbstractGunParserTest {

    @Override
    protected GunParser createParser() {
        return new SaxGunParser();
    }
}
