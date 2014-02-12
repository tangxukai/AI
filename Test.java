import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Stack;
class Edge{
	public Edge(int i, int w,Edge e){
		index = i;
		weight = w;//store int, so multiply 10 with the original data
		state = 0;//0 for initial state, 1 for frontier and 2 for explored.
		level = 0;
		next = e;
		parent = null;
	}

	public int index;
	public int state;
	public int weight;
	public int level;// not used in bfs, but must be used in uniform
	public Edge parent;
	public Edge next;
}
class LogInfo{
	public int index;
	public int level;
	public double cost;
	public LogInfo(int i, int l, double c){
		index = i;
		level = l;
		cost = c;
	}
}

public class Test {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		ArrayList<String> tie_break = new ArrayList<String>();
		try {
			BufferedReader in = new BufferedReader(new FileReader("tiebreaking2.txt"));
		    String str;
		    while ((str = in.readLine()) != null)
		        tie_break.add(str);
		    in.close();
		} catch (IOException e) {
		}
		
		ArrayList<Edge> graph = new ArrayList<Edge>();
		for(int i = 0; i < tie_break.size(); i++){
			graph.add(new Edge(i,0,null));
		}
		try {
			BufferedReader in = new BufferedReader(new FileReader("input2.txt"));
		    String str;
		    while ((str = in.readLine()) != null)
		    {
		    	//deal with the edge:Kevin,Frank,96
		    	String[] strs = str.split(",");
		    	int index1 = tie_break.indexOf(strs[0]);
		    	int index2 = tie_break.indexOf(strs[1]);
		    	double weight = Double.valueOf(strs[2]);
		    	Edge e1 = new Edge(index1,(int)(weight*10),null);
		    	Edge e2 = new Edge(index2,(int)(weight*10),null);
		    	Edge l1 = graph.get(index1);//represent the pointers from list
		    	Edge l2 = graph.get(index2);
		    	Edge current = l1;//begin insert e2 into the sorted linked list
		    	while(current.next!=null){
		    		if(e2.weight<current.next.weight){//sort weight first and when equal sort index
		    			Edge tmp = current.next;
		    			current.next = e2;
		    			e2.next = tmp;break;
		    		}
		    		else if(e2.weight == current.next.weight){
		    			if(e2.index<current.next.index){
		    				Edge tmp = current.next;
			    			current.next = e2;
			    			e2.next = tmp;break;
		    			}
		    			else current = current.next;
		    		}
		    		else current = current.next;
		    	}
		    	//deal with current.next == null
		    	current.next = e2;
		    	current = l2;//begin insert e2 into the sorted linked list
		    	while(current.next!=null){
		    		if(e1.weight<current.next.weight){//sort weight first and when equal sort index
		    			Edge tmp = current.next;
		    			current.next = e1;
		    			e1.next = tmp;break;
		    		}
		    		else if(e2.weight == current.next.weight){
		    			if(e2.index<current.next.index){
		    				Edge tmp = current.next;
			    			current.next = e1;
			    			e1.next = tmp;break;
		    			}
		    			else current = current.next;
		    		}
		    		else current = current.next;
		    	}
		    	//deal with current.next == null
		    	current.next = e1;
		    }
		    /*test whether the graph is correctly built
		     * by printing all the element in the adjacency list
		     * */
		    /*for(int i = 0; i < graph.size(); i++){
		    	System.out.print(graph.get(i).index+":\t");
		    	Edge test = graph.get(i).next;
		    	while(null!=test){
		    		System.out.print(test.index+","+test.weight+"\t");
		    		test = test.next;
		    	}
		    	System.out.println();
		    }*/
		    //use graph to BFS and DFS and uniform cost
		    in.close();
		} catch (IOException e) {
		}
		//begin searching algorithms
		//bfs(graph,0,1,tie_break);//bfs is done
		//uniform(graph,0,1,tie_break);//uniform is done
		//dfs(graph,0,1,tie_break);//dfs is done
	}
	/*dfs return 0 on success
	 * 
	 * */
	public static void dfs(ArrayList<Edge> graph, int start, int end, ArrayList<String> tie_break){
		//in dfs, there is only node that has been visited 1 or not visited 0
		ArrayList<LogInfo> list = new ArrayList<LogInfo>();
		Edge  node  = graph.get(start);
		node.state = 1;
		list.add(new LogInfo(node.index,0,node.weight));
		dfs_helper(list,node,graph,end,tie_break);
	}
	public static void dfs_helper(ArrayList<LogInfo> list1, Edge node, ArrayList<Edge> graph, int end, ArrayList<String> tie_break){
		ArrayList<LogInfo> list = new ArrayList<LogInfo>();
		list.addAll(list1);
		if(goal_test(node,end)){
			//write all info to the files
			Edge printer = node;
			
			Stack<String> stack = new Stack<String>();
			BufferedWriter bw = null,bw1 = null;
			
			try{
				File file = new File("output1_path_dfs.txt");
				File file1 = new File("output1_tlog_dfs.txt"); 
				// if file doesnt exists, then create it
				if (!file.exists()) {
					file.createNewFile();
				}
				if (!file1.exists()) {
					file1.createNewFile();
				}
				FileWriter fw = new FileWriter(file.getAbsoluteFile());
				FileWriter fw1 = new FileWriter(file1.getAbsoluteFile());
				bw = new BufferedWriter(fw);
				bw1 = new BufferedWriter(fw1);
			}catch(Exception e2){}
			while(printer!=null){
				String str = tie_break.get(printer.index);
				stack.push(str);
				printer = printer.parent;
			}
			
			while(!stack.isEmpty()){
			String path_node = stack.pop();
			try{
				bw.write(path_node+"\n");
			}catch(Exception e1){
			}
			}
			try {
				bw.close();
			} catch (IOException e2) {
				// TODO Auto-generated catch block
				e2.printStackTrace();
			}
			//start write log file
			try {
				bw1.write("name,depth,cost\n");
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			for(int i = 0; i < list.size(); i++){
				LogInfo log = list.get(i);
				try{
					bw1.write(tie_break.get(log.index)+","+log.level+","+log.cost/10.0+"\n");
				}catch(Exception e3){}
			}
			try{bw1.close();
			}catch(Exception e4){}
			return ;
			
		}
		else{
			Edge n = node.next;
			ArrayList<Edge> neighbours = new ArrayList<Edge>();// for traverse
			while(n!=null){
				if(graph.get(n.index).state==0){
					
					neighbours.add(graph.get(n.index));
					
				}
				n = n.next;
			}//end of while, end of getting all the neighbours
			//sort according to tie_break
			for(int i = 0; i < neighbours.size(); i++){
				for(int j = i+1; j < neighbours.size(); j++){
					if(neighbours.get(i).index>neighbours.get(j).index){
						Edge tmp = neighbours.get(i);
						neighbours.set(i,neighbours.get(j));
						neighbours.set(j, tmp);
					}
				}
			}
			//start to recursive
			for(int i = 0; i < neighbours.size(); i++){
				
				Edge e = neighbours.get(i);
				e.parent = node;
				e.state = 1;
				n = node.next;
				int ecost = 0;
				while(n!=null){
					if(n.index == e.index){
						ecost = n.weight;break;
					}
					n = n.next;
				}
				list.add(
						new LogInfo(e.index,
						list.get(list.size()-1).level+1,
						list.get(list.size()-1).cost + ecost)
						);
				dfs_helper(list,e,graph,end,tie_break);
			}
			
		}
	
	}
	/*uniform return 0 success -1 failure
	 * 
	 * */
	public static int uniform(ArrayList<Edge> graph, int start, int end, ArrayList<String> tie_break){
		LinkedList<Edge> queue = new LinkedList<Edge>();
		Queue<LogInfo> info_queue = new LinkedList<LogInfo>();
		Edge e = graph.get(start);
		e.state = 1;e.parent = null;//int level = 0;
		//info_queue.add(new LogInfo(e.index,e.level,e.weight));
		queue.add(e);
		//Edge dummy = new Edge(-1,0,null);
		//queue.add(dummy);//to control the value of level
		
		while(true){
			if(queue.isEmpty())return -1;
			//every time pop up you need to sort queue
			Edge checker = queue.getLast();
			if(queue.size()>=2){
			for(int i = 0; i < queue.size(); i++){
				for(int j = i+1; j < queue.size(); j++){
					if(queue.get(i).weight>queue.get(j).weight){
						Edge tmp = queue.get(i);
						queue.set(i,queue.get(j));
						queue.set(j, tmp);
					}
				}
			}
			}
			Edge n = queue.remove();
			//System.out.println(n.index);
			//add n into info_queue because it is the first it's visited.
			info_queue.add(new LogInfo(n.index,n.level,n.weight));
			//when remove() from queue, execute goal test
			if(goal_test(n,end)){
				//childnode.parent = n;
				Edge printer = n;
				Stack<String> stack = new Stack<String>();
				BufferedWriter bw = null,bw1 = null;
				
				try{
					File file = new File("output1_path_uniform.txt");
					File file1 = new File("output1_tlog_uniform.txt"); 
					// if file doesnt exists, then create it
					if (!file.exists()) {
						file.createNewFile();
					}
					if (!file1.exists()) {
						file1.createNewFile();
					}
					FileWriter fw = new FileWriter(file.getAbsoluteFile());
					FileWriter fw1 = new FileWriter(file1.getAbsoluteFile());
					bw = new BufferedWriter(fw);
					bw1 = new BufferedWriter(fw1);
				}catch(Exception e2){}
				while(printer!=null){
					
					String str = tie_break.get(printer.index);
					//System.out.println(str);
					stack.push(str);
					printer = printer.parent;
				}
				
				while(!stack.isEmpty()){
				String path_node = stack.pop();
				try{
					bw.write(path_node+"\n");
				}catch(Exception e1){
				}
				}
				try {
					bw.close();
				} catch (IOException e2) {
					// TODO Auto-generated catch block
					e2.printStackTrace();
				}
				//start write log file
				try {
					bw1.write("name,depth,cost\n");
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				
				while(info_queue.peek()!=null){
					LogInfo log = info_queue.poll();
					try{
						bw1.write(tie_break.get(log.index)+","+log.level+","+log.cost/10.0+"\n");
					}catch(Exception e3){}
				}
				try{bw1.close();
				}catch(Exception e4){}
				return 0;
			}//end of goal test
			
			n.state = 2;
			Edge child = n.next;//child iterates the neighbor vector of each node
			
			while(child!=null){
				int child_index = child.index;
				Edge childnode = graph.get(child_index);
				//childnode.parent = n;
					if(childnode.state==0){
						childnode.weight = n.weight + child.weight;//keep total cost
						childnode.level = n.level + 1;
						//info_queue.add(new LogInfo(childnode.index,level,childnode.weight));
						
						//before adding into frontier queue, we change the weight in childnode
						childnode.parent = n;
						childnode.state = 1;
						queue.add(childnode);
						
						
					}
					else if(childnode.state == 1 && (n.weight+child.weight)<childnode.weight){
						int i = queue.indexOf(childnode);
						Edge e1 = queue.get(i);
						e1.weight = n.weight+child.weight;
						e1.parent = n;
						e1.level = n.level + 1;
						e1.state = 1;
						
					}
					else{
						//==2 do nothing
					}
					child = child.next;
				
			}
		}//end of while
	}
	/*bfs return 0 success -1 failure
	 * 
	 * */
	public static int bfs(ArrayList<Edge> graph, int start, int end, ArrayList<String> tie_break){
		Queue<Edge> queue = new LinkedList<Edge>();
		Queue<LogInfo> info_queue = new LinkedList<LogInfo>();
		Edge e = graph.get(start);
		e.state = 1;e.parent = null;int level = 0;
		info_queue.add(new LogInfo(e.index,level,e.weight));
		//info_stack should push info before the goal test
		if(goal_test(e,end)){
			System.out.print(end);//
			return 0;
		}
		queue.add(e);
		Edge dummy = new Edge(-1,0,null);
		queue.add(dummy);//to control the value of level
		level = 1;//because we add nodes to queue and info_queue at the same time,so +1
		while(true){
			if(queue.isEmpty())return -1;
			Edge n = queue.remove();
			if(n == dummy && queue.size()!=0){level++;queue.add(n);continue;}
			if(n == dummy && queue.size()==0){return -1;}//means no solution found
			n.state = 2;
			Edge child = n.next;//child iterates the neighbor vector of each node
			while(child!=null){
				int child_index = child.index;
				Edge childnode = graph.get(child_index);
				if(childnode.state!=1&&childnode.state!=2){
					childnode.weight = n.weight + child.weight;//keep total cost
					
					info_queue.add(new LogInfo(childnode.index,level,childnode.weight));
					if(goal_test(childnode,end)){
						childnode.parent = n;
						Edge printer = childnode;
						Stack<String> stack = new Stack<String>();
						BufferedWriter bw = null,bw1 = null;
						
						try{
							File file = new File("output1_path_bfs.txt");
							File file1 = new File("output1_tlog_bfs.txt"); 
							// if file doesnt exists, then create it
							if (!file.exists()) {
								file.createNewFile();
							}
							if (!file1.exists()) {
								file1.createNewFile();
							}
							FileWriter fw = new FileWriter(file.getAbsoluteFile());
							FileWriter fw1 = new FileWriter(file1.getAbsoluteFile());
							bw = new BufferedWriter(fw);
							bw1 = new BufferedWriter(fw1);
						}catch(Exception e2){}
						while(printer!=null){
							String str = tie_break.get(printer.index);
							stack.push(str);
							printer = printer.parent;
						}
						
						while(!stack.isEmpty()){
						String path_node = stack.pop();
						try{
							bw.write(path_node+"\n");
						}catch(Exception e1){
						}
						}
						try {
							bw.close();
						} catch (IOException e2) {
							// TODO Auto-generated catch block
							e2.printStackTrace();
						}
						//start write log file
						try {
							bw1.write("name,depth,cost\n");
						} catch (IOException e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						}
						while(info_queue.peek()!=null){
							LogInfo log = info_queue.poll();
							try{
								bw1.write(tie_break.get(log.index)+","+log.level+","+log.cost/10.0+"\n");
							}catch(Exception e3){}
						}
						try{bw1.close();
						}catch(Exception e4){}
						return 0;
					}
					//before adding into frontier queue, we change the weight in childnode
					
					queue.add(childnode);
					
					childnode.parent = n;
					childnode.state = 1;
				}
				child = child.next;
			}
		}//end of while
	}
	public static boolean goal_test(Edge e,int goal){
		if(e.index==goal){return true;}
		return false;
	}
}
