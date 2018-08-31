	var p1;

	function fn_load_crom()
	{	
	  p1 = document.getElementById("plugin1");
		p1.xadl = v_xadl;        //xadl
		//p1.autosize = true;   //autosize
		p1.autosize = false;
		p1.commthreadcount = 30;         //commthreadcount
		p1.commthreadwaittime = 1000;   //commthreadwaittime
		p1.key = "EgovXPLATFORM";
		p1.run();				
		//alert(p1.getvariablevalue("VarUserID"));
	}

	function Callscript_Test()
	{
		//alert(p1.callscript("TestCallScript('abc')"));
	}
	function p1_onload(url)
	{
		//alert("p1.onload\n-" + url);
	}
	
	/***************************************
	   P  l  u  g  i  n    E  v  e  n  t
	***************************************/
	function my_loadtypedefinition(strurl)
	{
		//alert("loadtypedefinition : "+strurl);
	}

	function my_load()
	{
		//alert("load completed !!");
	}

	function my_loadingglobalvariables(strurl)
	{
		//alert("loadingglobalvariables : "+strurl);
	}

	function my_beforeexit(bCloseFlag, bHandledFlag)
	{
		//alert("beforeexit : " + bCloseFlag + bHandledFlag);
	}

	function my_exit()
	{
		//alert("¿Ã∫•∆Æ exit-");
	}

	function my_communication(bStart)
	{
		// alert("communication:"+bStart);	
	}

	function my_usernotify(nNotifyID, strMsg)
	{
	  //alert("nNotifyID:"+nNotifyID +" strMsg:"+strMsg); 
	}

	function my_error(nError, strErrMsg)
	{
		//alert("err:"+strErrMsg);
	}
	function my_addlog(strMsg)
	{
		//alert("strMsg:"+strMsg);
	}
	/***************************************
	 P  l  u  g  i  n    E  v  e  n  t (End)
	***************************************/



