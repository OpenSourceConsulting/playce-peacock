package com.athena.peacock.controller.web.alm.svn;
import java.io.File;
import java.io.FileFilter;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.tmatesoft.svn.core.SVNCommitInfo;
import org.tmatesoft.svn.core.SVNErrorCode;
import org.tmatesoft.svn.core.SVNErrorMessage;
import org.tmatesoft.svn.core.SVNException;
import org.tmatesoft.svn.core.SVNNodeKind;
import org.tmatesoft.svn.core.SVNURL;
import org.tmatesoft.svn.core.auth.ISVNAuthenticationManager;
import org.tmatesoft.svn.core.internal.io.dav.DAVRepositoryFactory;
import org.tmatesoft.svn.core.internal.io.fs.FSRepositoryFactory;
import org.tmatesoft.svn.core.internal.io.svn.SVNRepositoryFactoryImpl;
import org.tmatesoft.svn.core.io.ISVNEditor;
import org.tmatesoft.svn.core.io.SVNRepository;
import org.tmatesoft.svn.core.io.SVNRepositoryFactory;
import org.tmatesoft.svn.core.io.diff.SVNDeltaGenerator;
import org.tmatesoft.svn.core.wc.SVNWCUtil;


public class SvnImport {

    public static void main(String[] args) {
    	String SVN_URL = "http://localhost/svn/idkbj/PROJ3";
		String SVN_ID = "idkbj";
		String SVN_PW = "bong0524";
		String localRootDir = "D:\\project\\2014_hhi\\eclipse-jee-kepler-SR2-win32-x86_64\\workspace\\mavenweb";
		
        setupLibrary();
        
        /*
         * Run commit example and process error if any.
         */
        try {
        	importCommit(SVN_URL, SVN_ID, SVN_PW, localRootDir);
        } catch (SVNException e) {
            SVNErrorMessage err = e.getErrorMessage();
            /*
             * Display all tree of error messages. 
             * Utility method SVNErrorMessage.getFullMessage() may be used instead of the loop.
             */
            while(err != null) {
                System.err.println(err.getErrorCode().getCode() + " : " + err.getMessage());
                err = err.getChildErrorMessage();
            }
            e.printStackTrace();
            System.exit(1);
        }
        System.exit(0);
    }

    public static void importCommit(String svnUrl, String userName, String userPassword, String localRootDir) throws SVNException {
    	
    	File localRootFile = new File(localRootDir);
    	
    	
        /*
         * URL that points to repository. 
         */
        SVNURL url = SVNURL.parseURIEncoded(svnUrl);
        
        /*
         * Create an instance of SVNRepository class. This class is the main entry point 
         * for all "low-level" Subversion operations supported by Subversion protocol. 
         * 
         * These operations includes browsing, update and commit operations. See 
         * SVNRepository methods javadoc for more details.
         */
        SVNRepository repository = SVNRepositoryFactory.create(url);

        /*
         * User's authentication information (name/password) is provided via  an 
         * ISVNAuthenticationManager  instance.  SVNWCUtil  creates  a   default 
         * authentication manager given user's name and password.
         * 
         * Default authentication manager first attempts to use provided user name 
         * and password and then falls back to the credentials stored in the 
         * default Subversion credentials storage that is located in Subversion 
         * configuration area. If you'd like to use provided user name and password 
         * only you may use BasicAuthenticationManager class instead of default 
         * authentication manager:
         * 
         *  authManager = new BasicAuthenticationsManager(userName, userPassword);
         *  
         * You may also skip this point - anonymous access will be used. 
         */
        ISVNAuthenticationManager authManager = SVNWCUtil.createDefaultAuthenticationManager(userName, userPassword);
        repository.setAuthenticationManager(authManager);

        /*
         * Get type of the node located at URL we used to create SVNRepository.
         * 
         * "" (empty string) is path relative to that URL, 
         * -1 is value that may be used to specify HEAD (latest) revision.
         */
        SVNNodeKind nodeKind = repository.checkPath("", -1);

        /*
         * Checks  up  if the current path really corresponds to a directory. If 
         * it doesn't, the program exits. SVNNodeKind is that one who says  what
         * is located at a path in a revision. 
         */
        if (nodeKind == SVNNodeKind.NONE) {
            SVNErrorMessage err = SVNErrorMessage.create(SVNErrorCode.UNKNOWN, "No entry at URL ''{0}''", url);
            throw new SVNException(err);
        } else if (nodeKind == SVNNodeKind.FILE) {
            SVNErrorMessage err = SVNErrorMessage.create(SVNErrorCode.UNKNOWN, "Entry at URL ''{0}'' is a file while directory was expected", url);
            throw new SVNException(err);
        }
        
        ISVNEditor editor = repository.getCommitEditor("directory and file added", null);
    	
    	/*
         * Always called first. Opens the current root directory. It  means  all
         * modifications will be applied to this directory until  a  next  entry
         * (located inside the root) is opened/added.
         * 
         * -1 - revision is HEAD (actually, for a comit  editor  this number  is 
         * irrelevant)
         */
        editor.openRoot(-1);

        
        addFiles(editor, localRootFile, localRootDir, 0);
        
        
        /*
         * Closes the root directory.
         */
        editor.closeDir();
        
        SVNCommitInfo commitInfo = editor.closeEdit();
    	
    	System.out.println("all files was added: " + commitInfo);
        
    }

    
    private static void addDir(ISVNEditor editor, String relativeDir) throws SVNException{
    	
    	System.out.println("add : " + relativeDir);
    	/*
         * Adds a new directory (in this  case - to the  root  directory  for 
         * which the SVNRepository was  created). 
         * Since this moment all changes will be applied to this new  directory.
         * 
         * dirPath is relative to the root directory.
         * 
         * copyFromPath (the 2nd parameter) is set to null and  copyFromRevision
         * (the 3rd) parameter is set to  -1  since  the  directory is not added 
         * with history (is not copied, in other words).
         */
		editor.addDir(relativeDir, null, -1);
		
    }
    
    /*
     * This method performs commiting an addition of a file.
     */
    private static void addFile(ISVNEditor editor, String filePath, File targetFile) throws SVNException {
        
        
        if(filePath != null && targetFile != null){
        	
        	System.out.println("add : " + filePath);
        	
        	/*
             * Adds a new file to the just added  directory. The  file  path is also 
             * defined as relative to the root directory.
             *
             * copyFromPath (the 2nd parameter) is set to null and  copyFromRevision
             * (the 3rd parameter) is set to -1 since  the file is  not  added  with 
             * history.
             */
            editor.addFile(filePath, null, -1);
            /*
             * The next steps are directed to applying delta to the  file  (that  is 
             * the full contents of the file in this case).
             */
            editor.applyTextDelta(filePath, null);
            /*
             * Use delta generator utility class to generate and send delta
             * 
             * Note that you may use only 'target' data to generate delta when there is no 
             * access to the 'base' (previous) version of the file. However, using 'base' 
             * data will result in smaller network overhead.
             * 
             * SVNDeltaGenerator will call editor.textDeltaChunk(...) method for each generated 
             * "diff window" and then editor.textDeltaEnd(...) in the end of delta transmission.  
             * Number of diff windows depends on the file size. 
             *  
             */
            SVNDeltaGenerator deltaGenerator = new SVNDeltaGenerator();
            
            FileInputStream  target = null;
            
            try{
            	
            	target = new FileInputStream(targetFile);
            	
            	String checksum = deltaGenerator.sendDelta(filePath, target, editor, true);

                /*
                 * Closes the new added file.
                 */
                editor.closeFile(filePath, checksum);
                
                
            }catch(FileNotFoundException e){
            	throw new RuntimeException(e);
            }finally{
            	if(target != null){
            		try{
            			target.close();
            		}catch(IOException e){
            			//ignore.
            		}
            	}
            }
        	
        }
        
    }


    /*
     * Initializes the library to work with a repository via 
     * different protocols.
     */
    public static void setupLibrary() {
        /*
         * For using over http:// and https://
         */
        DAVRepositoryFactory.setup();
        /*
         * For using over svn:// and svn+xxx://
         */
        SVNRepositoryFactoryImpl.setup();
        
        /*
         * For using over file:///
         */
        FSRepositoryFactory.setup();
    }
    
    private static List<File> addFiles(ISVNEditor editor, File parentPath, String localRootDir, int depth)throws SVNException{
    	
    	if(depth > 0){
    		addDir(editor, getRelativePath(parentPath, localRootDir));
    	}
		
    	List<File> fileList = new ArrayList<File>();
    	
    	File[] files = parentPath.listFiles(new FileFilter() {
			
			public boolean accept(File arg0) {
				
				return arg0.isFile();
			}
		});
    	
		for (int i = 0; i < files.length; i++) {
			fileList.add(files[i]);
			
			addFile(editor, getRelativePath(files[i], localRootDir), files[i]);
			
		}
		
		File[] dirs = parentPath.listFiles(new FileFilter() {
			
			public boolean accept(File arg0) {
				
				return arg0.isDirectory() && !arg0.getPath().endsWith(".svn");
			}
		});
		
		for (int i = 0; i < dirs.length; i++) {
			fileList.add(dirs[i]);
			fileList.addAll(addFiles(editor, dirs[i], localRootDir, depth+1));
		}
		
		if(depth > 0){
			editor.closeDir();
		}
		
		return fileList;
	}
    
    private static String getRelativePath(File file, String localRootDir){
    	return file.getPath().substring(localRootDir.length()+1).replaceAll("\\\\", "/");
    }
}