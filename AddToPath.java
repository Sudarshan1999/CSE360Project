
public class AddToPath {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
		AddToPath();
	}
		
	
	
	// double linked list, .next used to handle right insertion
	// .prev handle left insertion
	public void AddToPath(Activity newNode)
	{
		// start
		if(newNode.dependencies.size() == 0)
		{
			pathList.add(newNode);
		}
		
		// read all dependencies of given node
		for(int i = 0; i < newNode.dependencies.size(); i++)
		{
			// reserved for all predecessors on the path
			string[] traversed;
			
			string dependency = newNode.dependencies[i];
			// loop through entire existing path
			for(int j = 0; j < pathList.size(); j++)
			{
				Activity start = pathList[j];
				// newNode is the 2nd in the path
				if(start.name.equals(dependency))
				{
					//
					newNode.prev = start;
					start.next = newNode;
				}
				
							
				else
				{
					// traverse down the path to find target node
					while(start.next != null)
					{
						traversed.add(start);
						
						
						if(start.name.equals(dependency))
						{
							// dependency already has a next, copy all previous traversed nodes
							// and create a new path
							if(start.next!= null)
							{
								// create new path and add the new start
								pathList.add(traversed[0]);
								int newIndex = pathList.size()-1;
								Activity newStart = pathList[newIndex];
								
								// linking new path start with the rest
								for(k = 1; k < traversed.size(); k++)
								{
									
									newStart.next = traversed[k];
									newStart = newStart.next;
								}
								newNode.prev = newStart;
								newStart.next = newNode;
								
							}
							//
							else
							{
								newNode.prev = start;
								start.next = newNode;
							}
							
						}
						start = start.next;						
					}
				}
			}
		}
	}
			
	public void DetectCycle(Activity[] pathList)
	{
		for(int i =0 ; i <pathList.size(); i ++)
		{
			int dupCount = 0;
			Activity current = pathList[i];
			for(int j = 0; j< pathList.size(); j++)
			{
				// check for duplicates, default to 1
				if(pathList[j]== current)
				{
					dupCount = dupCount + 1;
				}
			}
			
			if(dupCount > 1)
			{
				System.out.print("Cycle Detected");
				break;
				// some action
			}
			
		}
	}

	
}
