<?xml version="1.0" encoding="UTF-8"?>
<!--
    Splits the flat list of firearms into sub-lists by country of origin.

    Input:  <Gun> <firearm>..<origin>USA</origin>..</firearm> <firearm>..Ukraine..</firearm> ... </Gun>
    Output: <Gun> <USA> <firearm>..</firearm> ... </USA> <Ukraine> ... </Ukraine> </Gun>

    Grouping uses the Muenchian method (xsl:key + generate-id), because the JDK
    ships an XSLT 1.0 processor without xsl:for-each-group.
-->
<xsl:stylesheet version="1.0"
                xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
                xmlns="http://www.knu.ua/gun"
                xmlns:g="http://www.knu.ua/gun"
                xmlns:xalan="http://xml.apache.org/xslt"
                exclude-result-prefixes="g xalan">

    <xsl:output method="xml" encoding="UTF-8" indent="yes" xalan:indent-amount="4"/>
    <xsl:strip-space elements="*"/>

    <xsl:key name="firearms-by-origin" match="g:firearm" use="g:origin"/>

    <xsl:template match="/g:Gun">
        <Gun>
            <!-- first firearm of every origin represents its group -->
            <xsl:for-each select="g:firearm[generate-id() = generate-id(key('firearms-by-origin', g:origin)[1])]">
                <xsl:sort select="g:origin"/>
                <xsl:element name="{g:origin}">
                    <xsl:apply-templates select="key('firearms-by-origin', g:origin)"/>
                </xsl:element>
            </xsl:for-each>
        </Gun>
    </xsl:template>

    <!-- Copies an element without dragging along unused namespace declarations (e.g. xsi) -->
    <xsl:template match="g:*">
        <xsl:element name="{local-name()}">
            <xsl:copy-of select="@*"/>
            <xsl:apply-templates/>
        </xsl:element>
    </xsl:template>
</xsl:stylesheet>
