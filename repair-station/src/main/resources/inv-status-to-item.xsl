<?xml version="1.0" encoding="UTF-8" standalone="yes"?>
<xsl:stylesheet version="1.0"
	xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
	
<xsl:param name="itemStatus" />


<xsl:template match="item">
    <item>
        <title> <xsl:value-of select="title"/></title>
        <note> <xsl:value-of select="note"/></note>
        <quantity> <xsl:value-of select="quantity"/></quantity>
        <price> <xsl:value-of select="price"/></price>
        <status> <xsl:value-of select="$itemStatus"/></status>
    </item>
</xsl:template>


</xsl:stylesheet>