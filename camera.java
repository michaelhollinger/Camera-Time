/* Michael Hollinger - mi711587
COP 3503C - Computer Science II
Camera Time
2/24/2018
*/

import java.io.*;
import java.util.*;

class Camera
{
	static ArrayList<obj> objects = new ArrayList<obj>();

	//object class, where totalDist represents the net distance of an object from the origin
	static class obj implements Comparable<obj>
	{
		int x, y, totalDist;

		obj(int a, int b, int c)
		{
			x = a;
			y = b;
			totalDist = c;
		}

		//comparable class, using net distance
		@Override 
		public int compareTo(obj o)
		{
			return this.totalDist - o.totalDist;
		}
	}

	public static void main(String[] args) throws IOException
	{
		Scanner stdin = new Scanner(System.in);
		
		int numObjects, dist, start, end;

		//read in number of objects and distance from camera
		numObjects = stdin.nextInt();	
		dist = stdin.nextInt();
		stdin.nextLine();

		//read in beginning of window and end of window
		start = stdin.nextInt();
		end = stdin.nextInt();
		stdin.nextLine();

		//read in each object
		while(stdin.hasNextLine())
		{	
			int x = stdin.nextInt();
			int y = stdin.nextInt();
			int c = weightedDist(x, y, dist);
			objects.add(new obj(x, y, c));
			stdin.nextLine();
		}
		
		minCost(objects, numObjects, dist, start, end);
		stdin.close();
	}

	//greedy/brute force hybrid algorithm to determine the minimum cost of photographing every object on the graph
	public static void minCost(ArrayList<obj> objects, int numObjects, int dist, int start, int end)
	{	
		int numPictures = 0, numSeen = 0;

		//accounts for all objects within the same vicinity of each other. If a flag is asserted, then 
		//multiple pictures won't be taken for objects within the same region.
		boolean closeFlag = false;
		
		int i = 0;
		
		Collections.sort(objects);	

		while(!objects.isEmpty())
		{
			obj curObject = objects.get(0);
			
			//if it's in front of the great occluder.
			if(curObject.y < dist)
			{
				if(!closeFlag)
				{
					numPictures++;
					closeFlag = true;
				}
				
				numSeen++;
				objects.remove(objects.indexOf(curObject));
			}
			
			//if it's behind the start of the partition.
			else if(curObject.x < start)
			{
				numSeen++;
				numPictures++;
				objects.remove(objects.indexOf(curObject));
				
				for(int j = 0; j < objects.size(); j++)
				{
					if(objects.get(j).x < start && objects.get(j).y > dist)
					{
						numSeen++;
						objects.remove(j);
					}
				}
			}
			
			//if it's within the partition.
			else if(curObject.x >= start && curObject.x <= end)
			{
				numSeen++;
				numPictures++;
				objects.remove(objects.indexOf(curObject));
				
				for(int j = 0; j < objects.size(); j++)
				{
					if(objects.get(j).x >= start && objects.get(j).x <= end && objects.get(j).y > dist)
					{
						numSeen++;
						objects.remove(j);
					}
				}
			}
			
			//if it's past the partition.
			else if(curObject.x > end)
			{
				numSeen++;
				numPictures++;
				objects.remove(objects.indexOf(curObject));
				
				for(int j = 0; j < objects.size(); j++)
				{
					if(objects.get(j).x > end && objects.get(j).y > dist)
					{
						numSeen++;
						objects.remove(j);
					}
				}
			}			
			i++;			
		}						
		System.out.println(numPictures);
	}

	//utility function to find a rough estimate of the net distance between an object and the origin.
	public static int weightedDist(int a, int b, int dist)
	{
		int height, c;

		if(b < dist)
		{
			height = dist - b;
			c = (int) Math.sqrt(Math.pow(a, 2) + Math.pow(height, 2)); 
		}

		else if(b >= dist)
		{
			height = b;
			c = (int) Math.sqrt(Math.pow(a, 2) + Math.pow(height, 2));
		}

		else
		{
			return 0; 
		}
		return c;
	}
}
