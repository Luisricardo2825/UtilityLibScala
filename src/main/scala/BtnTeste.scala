import br.com.sankhya.extensions.actionbutton.{AcaoRotinaJava, ContextoAcao}
import br.com.sankhya.jape.core.JapeSession
import br.com.sankhya.modelcore.auth.AuthenticationInfo
import br.com.sankhya.modelcore.comercial.util.print.PrintManager
import br.com.sankhya.modelcore.comercial.util.print.converter.PrintConversionService
import br.com.sankhya.modelcore.comercial.util.print.model.PrintInfo
import br.com.sankhya.modelcore.util.{EntityFacadeFactory, ReportException, ReportManager}
import br.com.sankhya.sps.enumeration.DocTaste
import br.com.sankhya.ws.ServiceContext

import java.math.BigDecimal
import java.sql.SQLException
import java.util

class BtnImpressao extends AcaoRotinaJava {
  @throws[ReportException]
  @throws[SQLException]
  private def imprimirRelatorio(idPpd: BigDecimal, nuRel: BigDecimal): Unit = {
    val dwf = EntityFacadeFactory.getDWFFacade
    val jdbc = dwf.getJdbcWrapper
    jdbc.openSession()
    val reportParams = new util.HashMap[String, AnyRef]
    reportParams.put("IDPPD", idPpd)
    val report = ReportManager.getInstance.getReport(nuRel, dwf)
    val jasperPrint = report.buildJasperPrint(reportParams, jdbc.getConnection)
    val conteudo = PrintConversionService.getInstance.convert(jasperPrint, classOf[Array[Byte]]).asInstanceOf[Array[Byte]]
    val printManager = PrintManager.getInstance
    val authInfo = AuthenticationInfo.getCurrent
    val userId = authInfo.getUserID
    val userName = authInfo.getName
    val jobDescription = jasperPrint.getName
    val printInfo = new PrintInfo
    printInfo.setCopies(1)
    printInfo.setDocument(conteudo)
    printInfo.setDocTaste(DocTaste.JASPER)
    printInfo.setDocType(br.com.sankhya.sps.enumeration.DocType.RELATORIO)
    printInfo.setLocalPrinterName("SEM IMPRESSORA")
    printInfo.setJobDescription(jobDescription)
    printInfo.setUserId(userId)
    printInfo.setUserName(userName)
    printManager.print(printInfo)
  }

  @throws[Exception]
  override def doAction(contextoAcao: ContextoAcao): Unit = {
    val ctx = ServiceContext.getCurrent
    val idPpd = ctx.getJsonRequestBody.get("IDPPD")
    if (idPpd == null) throw new IllegalArgumentException("IDPPD cannot be null")
    val hnd = JapeSession.open
    imprimirRelatorio(idPpd.getAsBigDecimal, BigDecimal.valueOf(113))
    JapeSession.close(hnd)
  }
}