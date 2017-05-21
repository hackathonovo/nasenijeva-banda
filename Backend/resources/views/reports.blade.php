<div class="box">
 <div class="box-header">
   <h3 class="box-title">Volonteri</h3>

   <div class="box-tools">
     <ul class="pagination pagination-sm no-margin pull-right">
       <li><a href="#">&laquo;</a></li>
       <li><a href="#">1</a></li>
       <li><a href="#">2</a></li>
       <li><a href="#">3</a></li>
       <li><a href="#">&raquo;</a></li>
     </ul>
   </div>
 </div>
 <!-- /.box-header -->
 <div class="box-body no-padding">
   <table id="table1" class="table">
     <tr>
       <th style="width: 10px">#</th>
       <th>Volonter</th>
       <th>Stanica</th>
       <th>Br. akcija</th>
       <th>Vrijeme provedeno u akcijama (h)</th>
     </tr>
       @foreach($data as $row)
           <tr>
                <td>{{ $loop->iteration }}.</td>
                <td>{{ $row->name }}</td>
                <td>{{ $row->station->name }}</td>
                <td>{{  $numActions[$row->id]  }}</td>
               <td>{{  $totalHours[$row->id]  }}</td>
           </tr>
       @endforeach
   </table>
 </div>
 <!-- /.box-body -->
</div>
<!-- /.box -->