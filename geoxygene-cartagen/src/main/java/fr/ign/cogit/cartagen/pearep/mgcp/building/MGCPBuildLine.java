package fr.ign.cogit.cartagen.pearep.mgcp.building;

import java.util.HashMap;

import javax.persistence.Transient;

import org.hibernate.annotations.Type;

import fr.ign.cogit.cartagen.core.genericschema.urban.IBuildLine;
import fr.ign.cogit.cartagen.pearep.mgcp.MGCPFeature;
import fr.ign.cogit.cartagen.pearep.vmap.PeaRepDbType;
import fr.ign.cogit.geoxygene.api.feature.IFeature;
import fr.ign.cogit.geoxygene.api.spatial.coordgeom.ILineString;
import fr.ign.cogit.geoxygene.schemageo.api.bati.AutreConstruction;
import fr.ign.cogit.geoxygene.schemageo.impl.bati.AutreConstructionImpl;

public class MGCPBuildLine extends MGCPFeature implements IBuildLine {

  private AutreConstruction geoxObj;
  private String upd_date, upd_info, cpyrt_note, src_info, txt, nfi, nfn,
      tier_note, nam, src_date, ace, ale, wid, len;
  private long acc, ace_eval, ale_eval, uid, upd_name, src_name, zval_type,
      wle, fun;

  /**
   * @param type
   */
  public MGCPBuildLine(ILineString line, HashMap<String, Object> attributes,
      PeaRepDbType type) {
    super();
    this.setGeoxObj(new AutreConstructionImpl(line));
    this.setInitialGeom(line);
    this.setEliminated(false);

    // attributes present in Mgcp++
    this.acc = (Long) attributes.get("acc");
    this.ace_eval = (Long) attributes.get("ace_eval");
    this.ale_eval = (Long) attributes.get("ale_eval");
    if (attributes.containsKey("uid"))
      this.uid = (Long) attributes.get("uid");
    this.src_name = (Long) attributes.get("src_name");
    this.zval_type = (Long) attributes.get("zval_type");
    this.upd_date = (String) attributes.get("upd_date");
    this.upd_info = (String) attributes.get("upd_info");
    this.cpyrt_note = (String) attributes.get("cpyrt_note");
    this.upd_name = (Long) attributes.get("upd_name");
    this.wle = (Long) attributes.get("wle");
    this.src_info = (String) attributes.get("src_info");
    this.txt = (String) attributes.get("txt");
    this.nfi = (String) attributes.get("nfi");
    this.nfn = (String) attributes.get("nfn");
    this.tier_note = (String) attributes.get("tier_note");
    this.nam = (String) attributes.get("nam");
    this.src_date = (String) attributes.get("src_date");
    this.ace = (String) attributes.get("ace");
    this.ale = (String) attributes.get("ale");
    this.wid = (String) attributes.get("wid");
    if (attributes.containsKey("len"))
      this.len = (String) attributes.get("len");
    if (attributes.containsKey("fun"))
      this.fun = (Long) attributes.get("fun");
  }

  private void setGeoxObj(AutreConstruction elementIsole) {
    this.geoxObj = elementIsole;
  }

  @Override
  @Transient
  public IFeature getGeoxObj() {
    return this.geoxObj;
  }

  @Override
  @Type(type = "fr.ign.cogit.cartagen.software.interfaceCartagen.hibernate.GeOxygeneGeometryUserType")
  public ILineString getGeom() {
    return (ILineString) super.getGeom();
  }

  public String getUpd_date() {
    return upd_date;
  }

  public void setUpd_date(String upd_date) {
    this.upd_date = upd_date;
  }

  public String getUpd_info() {
    return upd_info;
  }

  public void setUpd_info(String upd_info) {
    this.upd_info = upd_info;
  }

  public String getCpyrt_note() {
    return cpyrt_note;
  }

  public void setCpyrt_note(String cpyrt_note) {
    this.cpyrt_note = cpyrt_note;
  }

  public String getSrc_info() {
    return src_info;
  }

  public void setSrc_info(String src_info) {
    this.src_info = src_info;
  }

  public String getTxt() {
    return txt;
  }

  public void setTxt(String txt) {
    this.txt = txt;
  }

  public String getNfi() {
    return nfi;
  }

  public void setNfi(String nfi) {
    this.nfi = nfi;
  }

  public String getNfn() {
    return nfn;
  }

  public void setNfn(String nfn) {
    this.nfn = nfn;
  }

  public String getTier_note() {
    return tier_note;
  }

  public void setTier_note(String tier_note) {
    this.tier_note = tier_note;
  }

  public String getNam() {
    return nam;
  }

  public void setNam(String nam) {
    this.nam = nam;
  }

  public String getSrc_date() {
    return src_date;
  }

  public void setSrc_date(String src_date) {
    this.src_date = src_date;
  }

  public String getAce() {
    return ace;
  }

  public void setAce(String ace) {
    this.ace = ace;
  }

  public String getAle() {
    return ale;
  }

  public void setAle(String ale) {
    this.ale = ale;
  }

  public String getWid() {
    return wid;
  }

  public void setWid(String wid) {
    this.wid = wid;
  }

  public String getLen() {
    return len;
  }

  public void setLen(String len) {
    this.len = len;
  }

  public long getAcc() {
    return acc;
  }

  public void setAcc(long acc) {
    this.acc = acc;
  }

  public long getAce_eval() {
    return ace_eval;
  }

  public void setAce_eval(long ace_eval) {
    this.ace_eval = ace_eval;
  }

  public long getAle_eval() {
    return ale_eval;
  }

  public void setAle_eval(long ale_eval) {
    this.ale_eval = ale_eval;
  }

  public long getUid() {
    return uid;
  }

  public void setUid(long uid) {
    this.uid = uid;
  }

  public long getUpd_name() {
    return upd_name;
  }

  public void setUpd_name(long upd_name) {
    this.upd_name = upd_name;
  }

  public long getSrc_name() {
    return src_name;
  }

  public void setSrc_name(long src_name) {
    this.src_name = src_name;
  }

  public long getZval_type() {
    return zval_type;
  }

  public void setZval_type(long zval_type) {
    this.zval_type = zval_type;
  }

  public long getWle() {
    return wle;
  }

  public void setWle(long wle) {
    this.wle = wle;
  }

  public long getFun() {
    return fun;
  }

  public void setFun(long fun) {
    this.fun = fun;
  }

}