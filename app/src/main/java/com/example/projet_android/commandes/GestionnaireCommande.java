package com.example.projet_android.commandes;

import com.example.projet_android.MainImage;

import java.util.ArrayList;
import java.util.List;

public class GestionnaireCommande {

    private static GestionnaireCommande instance;

    private List<ICommande> historiqueCmd;

    private List<ICommande> historiqueCmdAnnulees;

    private GestionnaireCommande() {
        this.historiqueCmd = new ArrayList<>();
        this.historiqueCmdAnnulees = new ArrayList<>();
    }

    public static GestionnaireCommande getInstance() {
        if (instance == null) {
            instance = new GestionnaireCommande();
        }
        return instance;
    }

    public void addCommande(ICommande cmd) {
        this.historiqueCmd.add(cmd);
    }

    public void addCommandeAnnulee(ICommande cmd) {
        this.historiqueCmdAnnulees.add(cmd);
    }

    public List<ICommande> getHistoriqueCmd() {
        return historiqueCmd;
    }

    public List<ICommande> getHistoriqueCmdAnnulees() {
        return historiqueCmdAnnulees;
    }

    public ICommande getLastCommand(List<ICommande> list) {
        if (!list.isEmpty()) {
            return list.remove(list.size() -1);
        }
        return null;
    }

    public void undoLastCommand(MainImage img) {
        ICommande cmd = this.getLastCommand(this.getHistoriqueCmd());
        if (cmd != null) {
            cmd.annuler(img);
            this.addCommandeAnnulee(cmd);
        }
    }

    public void redoCommand(MainImage img) {
        ICommande cmd = this.getLastCommand(this.historiqueCmdAnnulees);
        if (cmd != null) {
            cmd.retablir(img);
            this.addCommande(cmd);
        }
    }

    @Override
    public String toString() {
        return "GestionnaireCommande{" +
                "historiqueCmd=" + historiqueCmd +
                '}';
    }
}
