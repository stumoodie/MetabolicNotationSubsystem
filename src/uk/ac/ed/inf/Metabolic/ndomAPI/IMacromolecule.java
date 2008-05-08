
package uk.ac.ed.inf.Metabolic.ndomAPI;

import java.util.List;

public interface IMacromolecule extends IMolecule {
  String getGOTerm() ;
  String getUniProt() ;
  List<ICompound> getCompoundList() ;
  List<IMacromolecule> getSubunitList() ;
}
