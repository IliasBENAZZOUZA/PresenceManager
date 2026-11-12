package com.example.presencemanager   // khalli package dyalk

import android.content.Intent
import android.os.Bundle
import android.text.method.LinkMovementMethod
import android.text.util.Linkify
import android.view.GestureDetector
import android.view.Menu
import android.view.MenuItem
import android.view.MotionEvent
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.PopupMenu
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.view.ActionMode

class DashboardActivity : AppCompatActivity() {

    // ===== TP04 : CAB (hors de onCreate) =====
    private var actionMode: ActionMode? = null

    private val cabCallback = object : ActionMode.Callback {
        override fun onCreateActionMode(
            mode: ActionMode, menu: Menu
        ): Boolean {
            mode.menuInflater.inflate(R.menu.menu_cab_dashboard, menu)
            mode.title = "Actions contextuelles"
            return true
        }

        override fun onPrepareActionMode(
            mode: ActionMode, menu: Menu
        ): Boolean = false

        override fun onActionItemClicked(
            mode: ActionMode, item: MenuItem
        ): Boolean = when (item.itemId) {
            R.id.action_afficher -> {
                AlertDialog.Builder(this@DashboardActivity)
                    .setTitle("CAB")
                    .setMessage("Action Afficher exécutée.")
                    .setPositiveButton("OK", null)
                    .show()
                true
            }
            R.id.action_terminer -> {
                mode.finish()
                true
            }
            else -> false
        }

        override fun onDestroyActionMode(mode: ActionMode) {
            actionMode = null
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dashboard)

        val cardGroupes = findViewById<View>(R.id.cardGroupes)
        val cardAppel = findViewById<View>(R.id.cardAppel)
        val cardHistorique = findViewById<View>(R.id.cardHistorique)
        val cardStatistiques = findViewById<View>(R.id.cardStatistiques)

        // ===== TP03 - Étape 5 : clic simple =====
        cardGroupes.setOnClickListener {
            startActivity(Intent(this, GroupesActivity::class.java))
        }
        cardAppel.setOnClickListener {
            startActivity(Intent(this, AppelActivity::class.java))
        }
        cardHistorique.setOnClickListener {
            startActivity(Intent(this, HistoriqueActivity::class.java))
        }
        cardStatistiques.setOnClickListener {
            startActivity(Intent(this, StatistiquesActivity::class.java))
        }

        // ===== TP03 - Étape 6 : appui long (Groupes + Faire l'appel) =====
        cardGroupes.setOnLongClickListener {
            Toast.makeText(
                this,
                "Groupes : sélectionner ou consulter les groupes",
                Toast.LENGTH_SHORT
            ).show()
            true
        }
        cardAppel.setOnLongClickListener {
            Toast.makeText(
                this,
                "Faire l'appel : démarrer une séance",
                Toast.LENGTH_SHORT
            ).show()
            true
        }

        // ===== TP03 - Étape 7 : imgProfil (clic simple = autre app, double-clic = À propos) =====
        val imgProfil = findViewById<ImageView>(R.id.imgProfil)
        val detecteur = GestureDetector(
            this,
            object : GestureDetector.SimpleOnGestureListener() {
                override fun onDown(e: MotionEvent): Boolean = true

                override fun onSingleTapConfirmed(e: MotionEvent): Boolean {
                    ouvrirAutreApp()
                    return true
                }

                override fun onDoubleTap(e: MotionEvent): Boolean {
                    AlertDialog.Builder(this@DashboardActivity)
                        .setTitle("À propos de PrésenceManager")
                        .setMessage("Application de gestion des présences — Version 1.0")
                        .setPositiveButton("OK", null)
                        .show()
                    return true
                }
            }
        )
        imgProfil.setOnTouchListener { _, evenement ->
            detecteur.onTouchEvent(evenement)
            true
        }

        // ===== TP03 - Étape 8 : clic sur "Version 1.0" =====
        findViewById<TextView>(R.id.txtFooterDashboard).setOnClickListener {
            afficherInfosDeveloppeur()
        }

        // ===== TP04 - Étape 4 : bouton CAB =====
        val btnCab = findViewById<Button>(R.id.btnCab)
        btnCab.setOnClickListener {
            if (actionMode == null) {
                actionMode = startSupportActionMode(cabCallback)
            }
        }

        // ===== TP04 - Étape 6 : bouton Pop-up =====
        val btnPopup = findViewById<Button>(R.id.btnPopup)
        btnPopup.setOnClickListener {
            val popupMenu = PopupMenu(this, btnPopup)
            popupMenu.menuInflater.inflate(
                R.menu.menu_popup_dashboard, popupMenu.menu
            )
            popupMenu.setOnMenuItemClickListener { item ->
                when (item.itemId) {
                    R.id.action_ouvrir_groupes -> {
                        startActivity(
                            Intent(this, GroupesActivity::class.java)
                        )
                        true
                    }
                    R.id.action_informations -> {
                        AlertDialog.Builder(this)
                            .setTitle("PrésenceManager")
                            .setMessage(
                                "Menu contextuel flottant du tableau de bord."
                            )
                            .setPositiveButton("OK", null)
                            .show()
                        true
                    }
                    else -> false
                }
            }
            popupMenu.show()
        }
    }

    // Ouvre une autre app installée (bdl nomPackage b dyal l app dyalk)
    private fun ouvrirAutreApp() {
        val nomPackage = "com.whatsapp"
        val intention = packageManager.getLaunchIntentForPackage(nomPackage)

        if (intention != null) {
            startActivity(intention)
        } else {
            Toast.makeText(this, "Application non installée", Toast.LENGTH_SHORT).show()
        }
    }

    private fun afficherInfosDeveloppeur() {
        val dialogue = AlertDialog.Builder(this)
            .setTitle("Informations du développeur")
            .setMessage(
                "Application : PrésenceManager\n" +
                        "Version : 1.0\n" +
                        "Développeur : NOM_PRENOM\n" +
                        "GitHub : https://github.com/VOTRE_IDENTIFIANT/PresenceManager"
            )
            .setPositiveButton("OK", null)
            .show()

        dialogue.findViewById<TextView>(android.R.id.message)?.let { texte ->
            Linkify.addLinks(texte, Linkify.WEB_URLS)
            texte.movementMethod = LinkMovementMethod.getInstance()
        }
    }
}