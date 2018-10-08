
public class AddToPath {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
	}
	
	//doesn't check for cycle
	public void AddToPath(Activity newNode)
	{
		for(int i = 0; i < newNode.dependencies.size(); i++)
		{
			string[] traversed;
			string dependency = newNode.dependencies[i];
			// loop through entire existing path
			for(int j = 0; j < pathList.size(); j++)
			{
				Activity start = pathList[j];
				// newNode is the 2nd in the path
				if(start.name == dependency)
				{
					start.next = newNode;
				}
				else
				{
					while(start.next != null)
					{
						traversed.add(start);
						start = start.next;
						
						if(start.name == dependency)
						{
							// dependency already has a next, copy all previous traversed nodes
							// and create a new path
							if(start.next!= null)
							{
								// create new path and add the new start
								pathList.add(traversed[0]);
								int newIndex = pathList.size()-1;
								Activity newStart = pathList[newIndex];
								for(k = 1; k < traversed.size(); k++)
								{
									// linking new path start with the rest
									newStart.next = traversed[k];
									newStart = newStart.next;
								}
							}
							else
							{
								start.next = dependency;
							}
						}
					}
				}
			}
		}
	}


}
