package gui.common.base;

import gui.common.util.ErrorUtil;

import java.util.ArrayList;

public class NodeGroups extends SelUtil{
	
	/*
	 * METHODS
	 * 
	 */
	
	// Methods related to screen : NG
	
	
	
	// Methods related to screen : NG Add (Create)
	
	// Create node group.
	public static boolean createNodeGroups(String nodeGroupName, String nodeGroupDesc, ArrayList nodesInGroup){
		int noOfNodesInGroup = nodesInGroup.size();
		int noOfNodesInNodeListTable;
		String nodeToBeAdded = "";
		String nodeInTheNodeTable = "";
		String xPathOfIntheTable = "";
		String xPathOfNodeListTable = OR.getProperty("NodeGroups_CreateNodeGroup_Table_NodeList");
		
		printLogs("Calling createNodeGroup with values: " + nodeGroupName + ", " + nodeGroupDesc + ", " + nodesInGroup);
		
		try{
			// Go to Node Groups page
			if(!guiSetPage("NodeGroups")){
				printError("Failed to load Node Groups page.");
			}
						
			// Click on Create Node Group button
			if(!SelUtil.clickByXpath("NodeGroups_Button_CreateNodeGroup")){
				printError("Failed to click on create Node Group button.");
			}
			
			// Wait for create node group window
			if(!checkElementPresenceByXpath("NodeGroups_Heading_CreateNodeGroup")){
				printError("Failed to open create node group window");
			}
			captureScreenShot();
			
			// Enter the group name
			if(!SelUtil.sendKeysByXpath("NodeGroups_CreateNodeGroup_TextBox_NodeGroupName",nodeGroupName)){
				printError("Failed to enter node group name.");
			}
			
			// Enter the group description
			if(!SelUtil.sendKeysByXpath("NodeGroups_CreateNodeGroup_TextBox_NodeGroupDesc",nodeGroupDesc)){
				printError("Failed to enter node group description.");
			}
			
			// Get no. of nodes present in the node list table
			noOfNodesInNodeListTable = getTableRowCount("NodeGroups_CreateNodeGroup_Table_NodeList");
			
			// Select every node from the ArrayList
			for(int nodeCount = 0; nodeCount < noOfNodesInGroup; nodeCount++){
				
				// Get the node from the ArrayList
				nodeToBeAdded = (String) nodesInGroup.get(nodeCount);
				
				// Search for the node in every row of the table
				for(int rowCount = 1; rowCount <= noOfNodesInNodeListTable; rowCount++){
					xPathOfIntheTable = xPathOfNodeListTable + "/tr[" + rowCount + "]/td[2]";
					nodeInTheNodeTable = getTextByXpath(xPathOfIntheTable);
					
					// Compares node in the ArrayList with the node in the table
					if(nodeToBeAdded.contains(nodeInTheNodeTable)){
						
						// Click on the node on the table.
						if(!clickByXpath(xPathOfIntheTable)){
							printError("Unable To select the node: " + nodeToBeAdded);
						}
					}
				}
			}
			
			captureScreenShot();
						
			// Click on Add button
			if(!clickByXpath("NodeGroups_CreateNodeGroup_Button_Add")){
				printError("clickByXpath NodeGroups_CreateNodeGroup_Button_Add failed.");
				printFunctionReturn(fn_fail);
				return false;
			}
			
			// Wait for create node group to disappear
			if(!waitForNoElementByXpath("NodeGroups_Heading_CreateNodeGroup")){
				printLogs("Closing the Create node group popup screen using the Close button.");
				if(!SelUtil.clickByXpath("NodeGroups_CreateNodeGroup_Button_Close")) {
					printError("Failed to close the create node group window");
				}
			}
			
		}
		catch(Throwable t){
			ErrorUtil.addVerificationFailure(t);
			printError("Error occured while creating node group.");
			printFunctionReturn(fn_fail);
			return false;
		}
		printFunctionReturn(fn_pass);
		return true;
	}
	
	
	
	// Methods related to screen : NG Edit
	
	
	
	// Methods related to screen : NG Inventory 
	
	
	
	// Methods related to screen : NG Deploy
	
		
	
	// Methods related to screen : NG Reports 

}
