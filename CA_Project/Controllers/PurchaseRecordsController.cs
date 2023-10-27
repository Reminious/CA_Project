using System;
using System.Collections.Generic;
using System.Linq;
using System.Threading.Tasks;
using Microsoft.AspNetCore.Mvc;
using Microsoft.AspNetCore.Mvc.Rendering;
using Microsoft.EntityFrameworkCore;
using CA_Project.Models;

namespace CA_Project.Controllers
{
    public class PurchaseRecordsController : Controller
    {
        private readonly ShopContext _context;

        public PurchaseRecordsController(ShopContext context)
        {
            _context = context;
        }

        // GET: PurchaseRecords
        public async Task<IActionResult> Index()
        {
            var shopContext = _context.PurchaseRecords.Include(p => p.Product).Include(p => p.User);
            return View(await shopContext.ToListAsync());
        }

        // GET: PurchaseRecords/Details/5
        public async Task<IActionResult> Details(int? id)
        {
            if (id == null || _context.PurchaseRecords == null)
            {
                return NotFound();
            }

            var purchaseRecord = await _context.PurchaseRecords
                .Include(p => p.Product)
                .Include(p => p.User)
                .FirstOrDefaultAsync(m => m.UserId == id);
            if (purchaseRecord == null)
            {
                return NotFound();
            }

            return View(purchaseRecord);
        }

        // GET: PurchaseRecords/Create
        public IActionResult Create()
        {
            ViewData["ProductId"] = new SelectList(_context.Goods, "ProductId", "ProductId");
            ViewData["UserId"] = new SelectList(_context.Users, "UserId", "UserId");
            return View();
        }

        // POST: PurchaseRecords/Create
        // To protect from overposting attacks, enable the specific properties you want to bind to.
        // For more details, see http://go.microsoft.com/fwlink/?LinkId=317598.
        [HttpPost]
        [ValidateAntiForgeryToken]
        public async Task<IActionResult> Create([Bind("UserId,ProductId,ItemId,ActCode,Time")] PurchaseRecord purchaseRecord)
        {
            if (ModelState.IsValid)
            {
                _context.Add(purchaseRecord);
                await _context.SaveChangesAsync();
                return RedirectToAction(nameof(Index));
            }
            ViewData["ProductId"] = new SelectList(_context.Goods, "ProductId", "ProductId", purchaseRecord.ProductId);
            ViewData["UserId"] = new SelectList(_context.Users, "UserId", "UserId", purchaseRecord.UserId);
            return View(purchaseRecord);
        }

        // GET: PurchaseRecords/Edit/5
        public async Task<IActionResult> Edit(int? id)
        {
            if (id == null || _context.PurchaseRecords == null)
            {
                return NotFound();
            }

            var purchaseRecord = await _context.PurchaseRecords.FindAsync(id);
            if (purchaseRecord == null)
            {
                return NotFound();
            }
            ViewData["ProductId"] = new SelectList(_context.Goods, "ProductId", "ProductId", purchaseRecord.ProductId);
            ViewData["UserId"] = new SelectList(_context.Users, "UserId", "UserId", purchaseRecord.UserId);
            return View(purchaseRecord);
        }

        // POST: PurchaseRecords/Edit/5
        // To protect from overposting attacks, enable the specific properties you want to bind to.
        // For more details, see http://go.microsoft.com/fwlink/?LinkId=317598.
        [HttpPost]
        [ValidateAntiForgeryToken]
        public async Task<IActionResult> Edit(int id, [Bind("UserId,ProductId,ItemId,ActCode,Time")] PurchaseRecord purchaseRecord)
        {
            if (id != purchaseRecord.UserId)
            {
                return NotFound();
            }

            if (ModelState.IsValid)
            {
                try
                {
                    _context.Update(purchaseRecord);
                    await _context.SaveChangesAsync();
                }
                catch (DbUpdateConcurrencyException)
                {
                    if (!PurchaseRecordExists(purchaseRecord.UserId))
                    {
                        return NotFound();
                    }
                    else
                    {
                        throw;
                    }
                }
                return RedirectToAction(nameof(Index));
            }
            ViewData["ProductId"] = new SelectList(_context.Goods, "ProductId", "ProductId", purchaseRecord.ProductId);
            ViewData["UserId"] = new SelectList(_context.Users, "UserId", "UserId", purchaseRecord.UserId);
            return View(purchaseRecord);
        }

        // GET: PurchaseRecords/Delete/5
        public async Task<IActionResult> Delete(int? id)
        {
            if (id == null || _context.PurchaseRecords == null)
            {
                return NotFound();
            }

            var purchaseRecord = await _context.PurchaseRecords
                .Include(p => p.Product)
                .Include(p => p.User)
                .FirstOrDefaultAsync(m => m.UserId == id);
            if (purchaseRecord == null)
            {
                return NotFound();
            }

            return View(purchaseRecord);
        }

        // POST: PurchaseRecords/Delete/5
        [HttpPost, ActionName("Delete")]
        [ValidateAntiForgeryToken]
        public async Task<IActionResult> DeleteConfirmed(int id)
        {
            if (_context.PurchaseRecords == null)
            {
                return Problem("Entity set 'ShopContext.PurchaseRecords'  is null.");
            }
            var purchaseRecord = await _context.PurchaseRecords.FindAsync(id);
            if (purchaseRecord != null)
            {
                _context.PurchaseRecords.Remove(purchaseRecord);
            }
            
            await _context.SaveChangesAsync();
            return RedirectToAction(nameof(Index));
        }

        private bool PurchaseRecordExists(int id)
        {
          return (_context.PurchaseRecords?.Any(e => e.UserId == id)).GetValueOrDefault();
        }
    }
}
