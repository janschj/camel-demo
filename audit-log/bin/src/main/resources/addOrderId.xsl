<?xml version="1.0" ?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0">
 
  <!-- If an element is a child of library, copy the content and add
       type element. -->
       
 	<xsl:param name="orderId" />
       
  <xsl:template match="shiporder/*">
 		<xsl:attribute name="orderId">$orderId</xsl:attribute>
    <xsl:copy>
        <xsl:value-of select="name(.)"/>
 
      <xsl:call-template name="copy-children"/>
    </xsl:copy>
  </xsl:template>
 
  <!-- Copy the children of the current node. -->
  <xsl:template name="copy-children">
    <xsl:copy-of select="./*"/>
  </xsl:template>
 
  <!-- Generic identity template -->
  <xsl:template match="node()|@*">
    <xsl:copy>
      <xsl:apply-templates select="@*"/>
      <xsl:apply-templates/>
    </xsl:copy>
  </xsl:template>
</xsl:stylesheet>