package ru.xdweloper.bicompPreparer

import ru.xdweloper.bicompPreparer.fileWorkers.PropFileReader
import ru.xdweloper.bicompPreparer.mainProcess.startProcessing
import ru.xdweloper.bicompPreparer.utils.Message
import ru.xdweloper.bicompPreparer.utils.MessageType
import java.util.*

/**
 *@created 25 Август 2020 - 16:48
 *@project bicompPrepare
 *@author XDWeloper
 */


fun main(args: Array<String>) {

    //testContain()


    val propReader = PropFileReader()

    propReader.propFileList.forEach {
        Message.print("$_SERACHED_PROPFILE ${it.propFileName} $_Q_SERACHED_PROPFILE",MessageType.INFO)
        if(Scanner(System.`in`).next().also { Message.debug(it) }.toLowerCase() == "y"){
            startProcessing(it)
            }
    }
}

fun testContain() {
    var str1 = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
            "\n" +
            "<?import javafx.geometry.Insets?>\n" +
            "<?import javafx.scene.control.ButtonBar?>\n" +
            "<?import javafx.scene.control.Tab?>\n" +
            "<?import javafx.scene.control.TabPane?>\n" +
            "<?import javafx.scene.control.TableView?>\n" +
            "<?import javafx.scene.control.TitledPane?>\n" +
            "<?import javafx.scene.layout.AnchorPane?>\n" +
            "<?import javafx.scene.layout.ColumnConstraints?>\n" +
            "<?import javafx.scene.layout.GridPane?>\n" +
            "<?import javafx.scene.layout.HBox?>\n" +
            "<?import javafx.scene.layout.RowConstraints?>\n" +
            "<?import javafx.scene.layout.VBox?>\n" +
            "<?import ru.inversion.fx.form.controls.JInvButton?>\n" +
            "<?import ru.inversion.fx.form.controls.JInvCheckBox?>\n" +
            "<?import ru.inversion.fx.form.controls.JInvLabel?>\n" +
            "<?import ru.inversion.fx.form.controls.JInvRadioButton?>\n" +
            "<?import ru.inversion.fx.form.controls.JInvTable?>\n" +
            "<?import ru.inversion.fx.form.controls.JInvTableColumn?>\n" +
            "<?import ru.inversion.fx.form.controls.JInvTableColumnBigDecimal?>\n" +
            "<?import ru.inversion.fx.form.controls.JInvTextField?>\n" +
            "<?import ru.inversion.fx.form.controls.combobox.JInvComboBoxSimple?>\n" +
            "<?import ru.inversion.fx.form.lov.JInvLOVButton?>\n" +
            "\n" +
            "<VBox prefHeight=\"360.0\" prefWidth=\"640.0\" spacing=\"5.0\" xmlns=\"http://javafx.com/javafx/8.0.171\" xmlns:fx=\"http://javafx.com/fxml/1\" fx:controller=\"ru.inversion.bicomp.fxreport.apmain.ApMainController\">\n" +
            "<children>\n" +
            " <HBox alignment=\"CENTER_LEFT\" spacing=\"5.0\">\n" +
            " <children>\n" +
            " <JInvLabel minWidth=\"-Infinity\" text=\"%REPORT\" />\n" +
            " <JInvComboBoxSimple fx:id=\"REPORT_ID\" maxWidth=\"1.7976931348623157E308\" HBox.hgrow=\"ALWAYS\">\n" +
            " </JInvComboBoxSimple>\n" +
            " <JInvLOVButton fx:id=\"F9_LIST_REPORT_ID\" />\n" +
            " </children>\n" +
            " </HBox>\n" +
            " <TabPane fx:id=\"AP_MAIN_PG\" prefHeight=\"200.0\" prefWidth=\"200.0\" styleClass=\"floating\" tabClosingPolicy=\"UNAVAILABLE\" VBox.vgrow=\"ALWAYS\">\n" +
            " <tabs>\n" +
            " <Tab fx:id=\"PG_FORM\" text=\"%PG_FORM\">\n" +
            " <content>\n" +
            " <VBox prefHeight=\"200.0\" prefWidth=\"100.0\" spacing=\"5.0\">\n" +
            " <padding>\n" +
            " <Insets bottom=\"5.0\" left=\"5.0\" right=\"5.0\" top=\"5.0\" />\n" +
            " </padding>\n" +
            " <children>\n" +
            " <HBox spacing=\"5.0\" VBox.vgrow=\"ALWAYS\">\n" +
            " <children>\n" +
            " <TitledPane animated=\"false\" collapsible=\"false\" maxHeight=\"1.7976931348623157E308\" text=\"%TITLE_GENERATE\">\n" +
            " <content>\n" +
            " <GridPane hgap=\"5.0\" vgap=\"5.0\">\n" +
            " <columnConstraints>\n" +
            " <ColumnConstraints minWidth=\"18.0\" />\n" +
            " <ColumnConstraints hgrow=\"SOMETIMES\" />\n" +
            " </columnConstraints>\n" +
            " <rowConstraints>\n" +
            " <RowConstraints />\n" +
            " <RowConstraints />\n" +
            " <RowConstraints />\n" +
            " <RowConstraints />\n" +
            " <RowConstraints />\n" +
            " <RowConstraints />\n" +
            " <RowConstraints vgrow=\"ALWAYS\" />\n" +
            " </rowConstraints>\n" +
            " <children>\n" +
            " <JInvRadioButton fx:id=\"GENERATE_TYPE%%%DISPLAY\" fieldName=\"GENERATE_TYPE\" text=\"%GENERATE_TYPE.DISPLAY\" value=\"D\" GridPane.columnSpan=\"2147483647\" />\n" +
            " <JInvCheckBox fx:id=\"LOW_REGIM\" text=\"%LOW_REGIM\" GridPane.columnIndex=\"1\" GridPane.rowIndex=\"1\" />\n" +
            " <JInvCheckBox fx:id=\"EDIT_ENABLE\" layoutX=\"24.0\" layoutY=\"37.0\" text=\"%EDIT_ENABLE\" GridPane.columnIndex=\"1\" GridPane.rowIndex=\"2\" />\n" +
            " <JInvRadioButton fieldName=\"GENERATE_TYPE\" layoutX=\"19.0\" layoutY=\"19.0\" text=\"%GENERATE_TYPE.PRINTER\" value=\"P\" GridPane.columnSpan=\"2147483647\" GridPane.rowIndex=\"3\" />\n" +
            " <JInvRadioButton fieldName=\"GENERATE_TYPE\" layoutX=\"19.0\" layoutY=\"39.0\" text=\"%GENERATE_TYPE.FILE\" value=\"F\" GridPane.columnSpan=\"2147483647\" GridPane.rowIndex=\"4\" />\n" +
            " <JInvRadioButton fx:id=\"GENERATE_TYPE%%%VIEWER\" fieldName=\"GENERATE_TYPE\" layoutX=\"19.0\" layoutY=\"19.0\" text=\"%GENERATE_TYPE.VIEWER\" value=\"V\" GridPane.columnSpan=\"2147483647\" GridPane.rowIndex=\"5\" />\n" +
            " </children>\n" +
            " </GridPane>\n" +
            " </content></TitledPane>\n" +
            " <TitledPane animated=\"false\" collapsible=\"false\" maxHeight=\"1.7976931348623157E308\" text=\"%TITLE_FILE\" HBox.hgrow=\"ALWAYS\">\n" +
            " <content>\n" +
            " <VBox spacing=\"5.0\">\n" +
            " <padding>\n" +
            " <Insets bottom=\"5.0\" left=\"5.0\" right=\"5.0\" top=\"5.0\" />\n" +
            " </padding>\n" +
            " <children>\n" +
            " <JInvLabel text=\"%FILE_NAME\" />\n" +
            " <JInvTextField fx:id=\"FILE_NAME\" prefColumnCount=\"30\" />\n" +
            " <JInvRadioButton fx:id=\"DIR%%%MANUAL\" fieldName=\"DIR\" text=\"%DIR.MANUAL\" value=\"M\" />\n" +
            " <JInvRadioButton fx:id=\"DIR%%%USER_OUT\" fieldName=\"DIR\" value=\"O\" />\n" +
            " <JInvRadioButton fx:id=\"DIR%%%TEMP\" fieldName=\"DIR\" value=\"T\" />\n" +
            " </children>\n" +
            " </VBox>\n" +
            " </content></TitledPane>\n" +
            " </children>\n" +
            " </HBox>\n" +
            " <HBox alignment=\"CENTER_LEFT\" spacing=\"5.0\">\n" +
            " <children>\n" +
            " <JInvLabel minWidth=\"-Infinity\" text=\"%PRN_LIST\" />\n" +
            " <JInvComboBoxSimple fx:id=\"PRINTER_ID\" maxWidth=\"1.7976931348623157E308\" HBox.hgrow=\"ALWAYS\" />\n" +
            " </children>\n" +
            " </HBox>\n" +
            " </children>\n" +
            " </VBox>\n" +
            " </content>\n" +
            " </Tab>\n" +
            " <Tab fx:id=\"PG_PARAM\" text=\"%PG_PARAM\">\n" +
            " <content>\n" +
            " <AnchorPane>\n" +
            " <children>\n" +
            " <JInvTable fx:id=\"AP_REPORT_CAT_PARAM\" editable=\"true\" AnchorPane.bottomAnchor=\"5.0\" AnchorPane.leftAnchor=\"5.0\" AnchorPane.rightAnchor=\"5.0\" AnchorPane.topAnchor=\"5.0\">\n" +
            " <columns>\n" +
            " <JInvTableColumnBigDecimal fx:id=\"IPARAMNUM\" alignment=\"CENTER_RIGHT\" editable=\"false\" fieldName=\"IPARAMNUM\" maxWidth=\"800.0\" precision=\"0\" text=\"%IPARAMNUM\" transientColumn=\"false\" />\n" +
            " <JInvTableColumn fx:id=\"CPARAMDESCR\" editable=\"false\" fieldName=\"CPARAMDESCR\" maxWidth=\"2400.0\" text=\"%CPARAMDESCR\" transientColumn=\"false\" />\n" +
            " <JInvTableColumn fx:id=\"CPARAMVALUE\" maxWidth=\"3000.0\" text=\"%CPARAMVALUE\" transientColumn=\"false\" />\n" +
            " </columns>\n" +
            " <columnResizePolicy>\n" +
            " <TableView fx:constant=\"CONSTRAINED_RESIZE_POLICY\" />\n" +
            " </columnResizePolicy>\n" +
            " </JInvTable>\n" +
            " </children></AnchorPane>\n" +
            " </content>\n" +
            " </Tab>\n" +
            " </tabs>\n" +
            " </TabPane>\n" +
            "<ButtonBar buttonMinWidth=\"80.0\">\n" +
            "<buttons>\n" +
            "<JInvButton fx:id=\"btOK\" text=\"%OK\" />\n" +
            "<JInvButton fx:id=\"btCancel\" text=\"%CANCEL\" />\n" +
            "</buttons>\n" +
            "</ButtonBar>\n" +
            "</children>\n" +
            "<padding>\n" +
            "<Insets bottom=\"5.0\" left=\"5.0\" right=\"5.0\" top=\"5.0\" />\n" +
            "</padding>\n" +
            "</VBox>\n"

    var str2 =  "<ButtonBar buttonMinWidth=\"80.0\">\n" +
            " <buttons>"

//        str1 = str1.replace("$".toRegex(),"%%%")
//        str2 = str2.replace("$".toRegex(),"%%%")
        str2 = str2.trim()

        val isc = str1.contains(str2)
        val xxx1 = str1.substringBefore(str2)
        val xxx2 = str1.substringAfter(str2)
    val i = 0

}



