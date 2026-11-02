package ua.knu.gun.parser;

class StaxGunParserTest extends AbstractGunParserTest {

    @Override
    protected GunParser createParser() {
        return new StaxGunParser();
    }
}
