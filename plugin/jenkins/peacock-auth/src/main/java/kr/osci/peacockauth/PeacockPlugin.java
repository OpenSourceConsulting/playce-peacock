package kr.osci.peacockauth;

import hudson.Plugin;
import hudson.model.Api;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.kohsuke.stapler.export.Exported;
import org.kohsuke.stapler.export.ExportedBean;

@ExportedBean
public class PeacockPlugin extends Plugin {

    public Api getApi() {
        return new Api(this);
    }

    @Exported
    public String getInformation() {
    	
    	/*Set<String> sids = new HashSet<String>();
        for(Map.Entry entry : this.grantedRoles.entrySet()) {
          RoleMap roleMap = (RoleMap) entry.getValue();
          sids.addAll(roleMap.getSids(true));
        }
        //return sids;
*/        return "some string";
    }
}