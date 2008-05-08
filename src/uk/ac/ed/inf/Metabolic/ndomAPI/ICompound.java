
package uk.ac.ed.inf.Metabolic.ndomAPI;

public interface ICompound extends IMolecule {
  double getIC() ;
  String getInChI() ;
  String getCID() ;
  String getChEBIId() ;
  String getPubChemId() ;
  String getSmiles() ;
}
