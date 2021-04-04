package com.knoldus.common.amps

import com.crankuptheamps.client.{DefaultServerChooser, HAClient}

object AmpsClient {

  def getConnection(clientName: String, serverIpAddress: List[String]): HAClient = {
    val ampsHAClient = HAClient.createMemoryBacked(clientName)
    val serverChooser = new DefaultServerChooser()
    serverIpAddress.map(ipAddress => serverChooser.add(ipAddress))
    ampsHAClient.setServerChooser(serverChooser)
    ampsHAClient.connectAndLogon()
    ampsHAClient
  }
}
