<?xml version="1.0" encoding="UTF-8" standalone="yes"?>
<xsl:stylesheet version="1.0"
                xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
                xmlns="http://www.w3.org/TR/xhtml1/strict">

<xsl:strip-space elements="doc chapter section"/>
<xsl:output
   method="xml"
   indent="yes"
   encoding="iso-8859-1"
/>

<xsl:template match="shiporder">
     <shiporder>
     <orderperson>
       <xsl:value-of select="orderperson"/>
       </orderperson>
     </shiporder>
     <xsl:apply-templates/>
 
</xsl:template>

<xsl:template match="shipto">
    <shipto>
        <name> <xsl:value-of select="name"/></name>
        <address> <xsl:value-of select="address"/></address>
        <city> <xsl:value-of select="city"/></city>
        <country> <xsl:value-of select="country"/></country>
    </shipto>
</xsl:template>

<xsl:template match="item">
    <item>
        <title> <xsl:value-of select="title"/></title>
        <note> <xsl:value-of select="note"/></note>
        <quantity> <xsl:value-of select="quantity"/></quantity>
        <price> <xsl:value-of select="price"/></price>
    </item>
</xsl:template>


</xsl:stylesheet>